package com.burakenesdemir.stockmarket.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse dto = new ErrorResponse();
        dto.setResultCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        dto.setErrorMessage(ex.getMessage());
        dto.setResult(HttpStatus.INTERNAL_SERVER_ERROR.name());
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<Object> handleInternalServerError(
            InternalServerError ex, HttpHeaders headers, HttpStatus status, WebRequest request, HttpServletRequest httpRequest) {
        ErrorResponse dto = new ErrorResponse();
        dto.setResultCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        dto.setErrorMessage(ex.getMessage());
        dto.setResult(HttpStatus.INTERNAL_SERVER_ERROR.name());
        dto.setRequestUrl(httpRequest.getRequestURI());
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler(NoDataFoundError.class)
    public final ResponseEntity<Object> handleNoDataFoundError
            (NoDataFoundError ex, WebRequest request, HttpServletRequest httpRequest) {
        ErrorResponse dto = new ErrorResponse();
        dto.setResultCode(HttpStatus.UNAUTHORIZED.value());
        dto.setErrorMessage(ex.getMessage());
        dto.setResult(HttpStatus.UNAUTHORIZED.name());
        dto.setRequestUrl(httpRequest.getRequestURI());
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.UNAUTHORIZED, request);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<Object> handleBadRequestException
            (BadRequestException ex, WebRequest request, HttpServletRequest httpRequest) {
        ErrorResponse dto = new ErrorResponse();
        dto.setResultCode(HttpStatus.BAD_REQUEST.value());
        dto.setErrorMessage(ex.getMessage());
        dto.setResult(HttpStatus.BAD_REQUEST.name());
        dto.setRequestUrl(httpRequest.getRequestURI());
        return handleExceptionInternal(ex, dto, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }
}