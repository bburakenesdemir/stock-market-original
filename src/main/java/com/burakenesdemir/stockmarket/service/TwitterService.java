package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.data.*;
import com.burakenesdemir.stockmarket.exception.BadRequestException;
import com.burakenesdemir.stockmarket.resource.TweetResource;
import com.burakenesdemir.stockmarket.security.api.SecurityService;
import com.burakenesdemir.stockmarket.type.enums.CoinEnum;
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

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static com.burakenesdemir.stockmarket.util.ErrorUtil.*;

@Service
@AllArgsConstructor
public class TwitterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterService.class);

    private final RedisRepository redisRepository;

    private final SecurityService securityService;

    private final UserRepository userRepository;

    private final TransactionRecordRepository transactionRepository;

    private final AnalyzeService analyzeService;

    public List<TweetResource> getTweetsByHashtag(CoinEnum coin) {
        List<TweetResource> tweetList;
        Map<String, TweetResource> map = new HashMap<>();

        try {
            if (hashtagControl(coin.getName())) {
                Date date = new Date();
                TransactionRecord transactionRecord = transactionRepository.getTransactionRecordByHashtag(coin.getName());

                if (DateUtils.addMinutes(transactionRecord.getSearchTime(), 5).after(date)) {
                    transactionRecord.setSearchCount(transactionRecord.getSearchCount() + 1);
                    transactionRepository.save(transactionRecord);
                    return redisRepository.findAll(coin.getName());
                } else {
                    clearCache(coin.getName());
                    tweetList = scrapingTwitter(coin.getName(), 1);
                    saveTransactionRecord(coin.getName(), tweetList);

                    MapUtils.populateMap(map, tweetList, TweetResource::getId);

                    redisRepository.save(map, coin.getName());

                }

            } else {
                tweetList = scrapingTwitter(coin.getName(), 1);
                saveTransactionRecord(coin.getName(),tweetList);
                MapUtils.populateMap(map, tweetList, TweetResource::getId);

                redisRepository.save(map, coin.getName());
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
        String resourceUrl = ConfigLoaderUtil.getProperty("stock-market.nodejs.twitter.uri") + "query-short?query=" + hashtag + "&scroll=" + scrollSize;
        //String resourceUrl = "http://localhost:8080/test";
        ResponseEntity<List<TweetResource>> tweetResponse;
        Instant start = Instant.now();
        tweetResponse = restTemplate.exchange(resourceUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        Instant end = Instant.now();

        LOGGER.info("Scraping Twitter Time : " + Duration.between(start, end));

        User user = getCurrentUser();
        user.setLastHashTag(hashtag);
        userRepository.save(user);

        List<TweetResource> processedTweetList = tweetResponse.getBody();

        if (processedTweetList == null) {
            throw new BadRequestException(NOT_FOUND_TWEET);
        }


        processedTweetList = analyzeService.sentimentAnalyzeTweet(cleanTweets(processedTweetList));

        return processedTweetList;
    }

    private void saveTransactionRecord(String hashtag, List<TweetResource> tweetResources) {
        TransactionRecord currentTransaction = transactionRepository.getTransactionRecordByHashtag(hashtag);
        TransactionRecord newTransaction = new TransactionRecord();

        if (currentTransaction != null) {
            currentTransaction.setSearchTime(new Date());
            currentTransaction.setSearchCount(currentTransaction.getSearchCount() + 1);
            currentTransaction.setScore(calculateScorePointOfTweets(tweetResources));
            transactionRepository.save(currentTransaction);
        } else {
            newTransaction.setHashtag(hashtag);
            newTransaction.setSearchTime(new Date());
            newTransaction.setSearchCount(1);
            newTransaction.setScore(calculateScorePointOfTweets(tweetResources));
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

    private List<TweetResource> cleanTweets(List<TweetResource> tweetResource) {

        tweetResource.forEach(tweet -> tweet.setText(tweet.getText().trim()
                // remove links
                .replaceAll("http.*?[\\S]+", "")
                // remove usernames
                .replaceAll("@[\\S]+", "")
                // replace hashtags by just words
                .replaceAll("#", "")
                //delete url in text
                .replaceAll("(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", "")
                //delete usernames
                .replaceAll("^\\\"@[a-zA-Z]+1\\\"$", "")
                // correct all multiple white spaces to a single white space
                .replaceAll("[\\s]+", " ")));

        return tweetResource;
    }

    private Float calculateScorePointOfTweets(List<TweetResource> tweetList) {
        AtomicReference<Float> score = new AtomicReference<>((float) 0);
        tweetList.forEach(tweet -> score.updateAndGet(sentiment -> sentiment + tweet.getSentiment()));
        LOGGER.info("General sentiment score calculated!");
        return score.get() / tweetList.size();
    }
}

