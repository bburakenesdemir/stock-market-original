package com.burakenesdemir.stockmarket.resource;

import com.burakenesdemir.stockmarket.dto.DocumentSentimentDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class AnalyzeSentimentResponse {

    @JsonProperty("documentSentiment")
    private DocumentSentimentDto documentSentiment;
}
