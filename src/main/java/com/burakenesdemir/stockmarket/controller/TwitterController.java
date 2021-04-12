package com.burakenesdemir.stockmarket.controller;

import com.burakenesdemir.stockmarket.resource.TweetResource;
import com.burakenesdemir.stockmarket.service.TwitterService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/twitter")
@AllArgsConstructor
public class TwitterController {

    private final TwitterService twitterService;

    @GetMapping(value = "/get-tweets/hashtag/{hashtag}/scroll-size/{scrollSize}")
    public List<TweetResource> getTweets(@PathVariable String hashtag,
                                         @PathVariable Integer scrollSize) {
        return twitterService.getTweetsByHashtag(hashtag, scrollSize);
    }

    @GetMapping(value = "/get-all/hashtag/{hashtag}")
    public List<TweetResource> getAll(@PathVariable String hashtag) {
        return twitterService.getAll(hashtag);
    }
}