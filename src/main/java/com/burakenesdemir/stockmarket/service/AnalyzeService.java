package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.dto.SentimentRequest;
import com.burakenesdemir.stockmarket.resource.AnalyzeSentimentResponse;
import com.burakenesdemir.stockmarket.resource.Document;
import com.burakenesdemir.stockmarket.resource.TweetResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
public class AnalyzeService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterService.class);

    public List<TweetResource> sentimentAnalyzeTweet(List<TweetResource> processedTweetList) {
        Instant start = Instant.now();
        processedTweetList.parallelStream().forEach(AnalyzeService::getSentimentScore);
        Instant end = Instant.now();

        LOGGER.info("Sentiment Analysis Time : "  + Duration.between(start,end));

        return processedTweetList;
    }

    public static void getSentimentScore(TweetResource tweetResource){
        RestTemplate restTemplate = new RestTemplate();
        SentimentRequest request = new SentimentRequest();
        Document document = new Document();

        String url = "https://language.googleapis.com/v1beta2/documents:analyzeSentiment?key=AIzaSyA_6gAVSsa3vgWZlHeTRodsDQ6x7-bQxqU";
        document.setLanguage("EN");
        document.setType("PLAIN_TEXT");
        request.setEncodingType("UTF8");

        document.setContent(tweetResource.getText());
        request.setDocument(document);
        ResponseEntity<AnalyzeSentimentResponse> responseEntity = restTemplate.postForEntity(url, request, AnalyzeSentimentResponse.class);
        tweetResource.setSentiment(responseEntity.getBody().getDocumentSentiment().getScore());
    }
    /*

    public static Sentiment analyzeSentimentText(String text) throws Exception {
        // [START language_sentiment_text]
        // Instantiate the Language client com.google.cloud.language.v1.LanguageServiceClient
        try (LanguageServiceClient language = LanguageServiceClient.create()) {
            Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
            AnalyzeSentimentResponse response = language.analyzeSentiment(doc);
            Sentiment sentiment = response.getDocumentSentiment();
            if (sentiment == null) {
                System.out.println("No sentiment found");
            } else {
                System.out.printf("Sentiment magnitude: %.3f\n", sentiment.getMagnitude());
                System.out.printf("Sentiment score: %.3f\n", sentiment.getScore());
            }
            return sentiment;
        }
        // [END language_sentiment_text]
    }

    public void analyze(){
        StanfordCoreNLP pipeline = Pipeline.getPipeline();
        Annotation annotation = pipeline.process("I don`t like this place.");
        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
            System.out.println(RNNCoreAnnotations.getPredictedClass(tree));
        }

    }
    */


}

