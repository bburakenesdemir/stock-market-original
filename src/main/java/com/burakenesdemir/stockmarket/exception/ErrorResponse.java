package com.burakenesdemir.stockmarket.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String errorMessage;

    private Integer resultCode;

    private String result;

    private String requestUrl;
}