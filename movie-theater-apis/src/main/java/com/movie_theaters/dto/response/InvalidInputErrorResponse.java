package com.movie_theaters.dto.response;

import java.util.Map;

import org.springframework.http.HttpStatus;

public class InvalidInputErrorResponse {
    private String errorCode;
    private String message;
    private HttpStatus status;
    private Map<String, String> errors;

    public InvalidInputErrorResponse(String errorCode, String message, HttpStatus status) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
    }

    public InvalidInputErrorResponse(String errorCode, String message, HttpStatus status, Map<String, String> errors) {
        this.errorCode = errorCode;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }
}
