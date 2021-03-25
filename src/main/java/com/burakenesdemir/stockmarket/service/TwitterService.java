package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.exception.BadRequestException;
import com.burakenesdemir.stockmarket.resource.TweetResource;
import com.burakenesdemir.stockmarket.util.ConfigLoaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static com.burakenesdemir.stockmarket.util.ErrorUtil.TWITTER_API_ERROR;

@Service
public class TwitterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterService.class);

    @Cacheable(cacheNames = "tweetsCache")
    public List<TweetResource> getTweetsByHashtag(String hashtag, Integer scrollSize) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = ConfigLoaderUtil.getProperty("stock-market.nodejs.twitter.uri") + "query?query=" + hashtag + "&scroll=" + scrollSize;

        ResponseEntity<List<TweetResource>> tweetResponse;
        List<TweetResource> tweetList;
        try {
            Thread.sleep(5000);
            tweetResponse = restTemplate.exchange(resourceUrl,
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<>() {
                    });

            tweetList = tweetResponse.getBody();

        } catch (RestClientResponseException | InterruptedException ex) {
            throw new BadRequestException(TWITTER_API_ERROR);
        }

        return tweetList;
    }

    @CacheEvict(cacheNames = "tweetsCache", allEntries = true)
    public void clearCache(){
        LOGGER.info("Cache was cleaned!");
    }
}
