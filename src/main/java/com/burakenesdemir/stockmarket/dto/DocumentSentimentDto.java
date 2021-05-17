package com.burakenesdemir.stockmarket.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
public class DocumentSentimentDto {

    @JsonProperty("magnitude")
    private Float magnitude;

    @JsonProperty("score")
    private Float score;
}
