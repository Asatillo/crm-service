package com.example.CRM.payload;

import lombok.Data;

import java.time.Instant;
import java.util.HashMap;

@Data
public class ExceptionResponse {
    private String error;
    private Integer status;
    private String message;
    private HashMap<String, String> validationErrors = new HashMap<>();
    private Instant timestamp;

    public ExceptionResponse(String message, String error, Integer status) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = Instant.now();
    }

    public ExceptionResponse(String message, String error, Integer status, HashMap<String, String> validationErrors) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = Instant.now();
        this.validationErrors = validationErrors;
    }
}