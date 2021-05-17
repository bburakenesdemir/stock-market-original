package com.burakenesdemir.stockmarket.dto;

import com.burakenesdemir.stockmarket.resource.Document;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class SentimentRequest {

    @JsonProperty("document")
    private Document document;

    @JsonProperty("encodingType")
    private String encodingType;
}
