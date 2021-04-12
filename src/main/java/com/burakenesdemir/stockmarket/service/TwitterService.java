package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.data.*;
import com.burakenesdemir.stockmarket.exception.BadRequestException;
import com.burakenesdemir.stockmarket.resource.TweetResource;
import com.burakenesdemir.stockmarket.security.api.SecurityService;
import com.burakenesdemir.stockmarket.util.ConfigLoaderUtil;
import lombok.AllArgsConstructor;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.burakenesdemir.stockmarket.util.ErrorUtil.*;

@Service
@AllArgsConstructor
public class TwitterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterService.class);
    private final RedisRepository redisRepository;
    private final SecurityService securityService;
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRepository;

    public List<TweetResource> getTweetsByHashtag(String hashtag, Integer scrollSize) {
        List<TweetResource> tweetList;
        Map<String, TweetResource> map = new HashMap<>();

        try {
            if (hashtagControl(hashtag)) {
                Date date = new Date();
                TransactionRecord transactionRecord = transactionRepository.getTransactionRecordByHashtag(hashtag);

                    if (DateUtils.addMinutes(transactionRecord.getSearchTime(), 5).after(date)) {
                        return redisRepository.findAll(hashtag); //TODO NLP'DE KULLANILACAK.
                    } else {
                        clearCache(hashtag);
                        tweetList = scrapingTwitter(hashtag, scrollSize);
                        MapUtils.populateMap(map, tweetList, TweetResource::getId);
                        redisRepository.save(map, hashtag);
                    }

            } else {
                tweetList = scrapingTwitter(hashtag, scrollSize);

                if (tweetList == null) {
                    throw new BadRequestException(NOT_FOUND_TWEET);
                }

                MapUtils.populateMap(map, tweetList, TweetResource::getId);
                redisRepository.save(map, hashtag);
            }
        } catch (RestClientResponseException e) {
            throw new BadRequestException(TWITTER_API_ERROR);
        }
        return tweetList;
    }

    /**
     * @param hashtag search key in twitter
     * @return Hashtag been searched before?
     */
    private boolean hashtagControl(String hashtag) {
        return transactionRepository.getTransactionRecordByHashtag(hashtag) != null;
    }

    private List<TweetResource> scrapingTwitter(String hashtag, Integer scrollSize) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = ConfigLoaderUtil.getProperty("stock-market.nodejs.twitter.uri") + "query?query=" + hashtag + "&scroll=" + scrollSize;
        ResponseEntity<List<TweetResource>> tweetResponse;

        tweetResponse = restTemplate.exchange(resourceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });

        User user = getCurrentUser();
        user.setLastHashTag(hashtag);
        userRepository.save(user);

        saveTransactionRecord(hashtag);

        return tweetResponse.getBody();
    }

    private void saveTransactionRecord(String hashtag) {
        TransactionRecord currentTransaction = transactionRepository.getTransactionRecordByHashtag(hashtag);
        TransactionRecord newTransaction = new TransactionRecord();

        if (currentTransaction != null) {
            currentTransaction.setSearchTime(new Date());
            transactionRepository.save(currentTransaction);
        }else{
            newTransaction.setHashtag(hashtag);
            newTransaction.setSearchTime(new Date());
            transactionRepository.save(newTransaction);
        }
    }

    public List<TweetResource> getAll(String hashtag) {
        return redisRepository.findAll(hashtag);
    }

    public void clearCache(String hashtag) {
        LOGGER.info("Cache was cleaned!");
        redisRepository.deleteAll(hashtag);
    }

    public User getCurrentUser() {
        User user = userRepository.findByUsername(securityService.getUsername());
        if (user == null) {
            throw new BadRequestException(USER_NOT_FOUND_ERROR);
        }
        return user;
    }
}
