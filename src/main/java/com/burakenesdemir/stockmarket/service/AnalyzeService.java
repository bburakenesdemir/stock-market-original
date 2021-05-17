package com.burakenesdemir.stockmarket.service;

import com.burakenesdemir.stockmarket.dto.SentimentRequest;
import com.burakenesdemir.stockmarket.resource.AnalyzeSentimentResponse;
import com.burakenesdemir.stockmarket.resource.Document;
import com.burakenesdemir.stockmarket.resource.TweetResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class AnalyzeService {

    public List<TweetResource> sentimentAnalyzeTweet(List<TweetResource> processedTweetList) {
        RestTemplate restTemplate = new RestTemplate();
        SentimentRequest request = new SentimentRequest();
        Document document = new Document();
        String url = "https://language.googleapis.com/v1beta2/documents:analyzeSentiment?key=AIzaSyA_6gAVSsa3vgWZlHeTRodsDQ6x7-bQxqU";

        for(TweetResource tweet : processedTweetList){
            document.setLanguage("EN");
            document.setContent(tweet.getText());
            document.setType("PLAIN_TEXT");

            request.setDocument(document);
            request.setEncodingType("UTF8");

            ResponseEntity<AnalyzeSentimentResponse> responseEntity = restTemplate.postForEntity(url, request, AnalyzeSentimentResponse.class);

            tweet.setSentiment(responseEntity.getBody().getDocumentSentiment().getScore());
        }

        return processedTweetList;
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

