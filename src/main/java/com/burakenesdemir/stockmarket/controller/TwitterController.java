package com.burakenesdemir.stockmarket.controller;

import com.burakenesdemir.stockmarket.resource.TweetResource;
import com.burakenesdemir.stockmarket.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/twitter")
public class TwitterController {

    @Autowired
    TwitterService twitterService;

    @GetMapping(value = "/get-tweets/hashtag/{hashtag}/scroll-size/{scrollSize}")
    public List<TweetResource> getTweets(@PathVariable String hashtag,
                                         @PathVariable Integer scrollSize) {
        return twitterService.getTweetsByHashtag(hashtag, scrollSize);
    }

    @PostMapping(value = "/clear-cache")
    @Scheduled(fixedRate = 1000)
    public void clearCache(){
        twitterService.clearCache();
    }
}