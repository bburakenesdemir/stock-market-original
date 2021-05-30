package com.burakenesdemir.stockmarket.controller;

import com.burakenesdemir.stockmarket.data.TransactionRecord;
import com.burakenesdemir.stockmarket.data.TransactionRecordRepository;
import com.burakenesdemir.stockmarket.resource.TweetResource;
import com.burakenesdemir.stockmarket.service.AnalyzeService;
import com.burakenesdemir.stockmarket.service.TwitterService;
import com.burakenesdemir.stockmarket.type.enums.CoinEnum;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/twitter")
@AllArgsConstructor
public class TwitterController {

    private final TwitterService twitterService;

    private final AnalyzeService analyzeService;

    private final TransactionRecordRepository transactionRecordRepository;

    @GetMapping(value = "/get-tweets")
    @Transactional
    public List<TweetResource> getTweets(@RequestParam CoinEnum coinEnum) {
        return twitterService.getTweetsByHashtag(coinEnum);
    }

    @GetMapping(value = "/get-all/hashtag/{hashtag}")
    public List<TweetResource> getAll(@PathVariable String hashtag) {
        return twitterService.getAll(hashtag);
    }

    @GetMapping(value = "/sentiment")
    public void sentimentAnalysis() throws IOException, InterruptedException {
        analyzeService.sentimentAnalyzeTweet(null);
    }

    @GetMapping(value = "/get-transaction-records")
    public List<TransactionRecord> getTransactionRecords(){
        return transactionRecordRepository.findAll();
    }
}