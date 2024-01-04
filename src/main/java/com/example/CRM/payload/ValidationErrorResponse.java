package com.example.CRM.payload;

import lombok.Data;

import java.time.Instant;
import java.util.HashMap;

@Data
public class ValidationErrorResponse {
    private String error;
    private Integer status;
    private String message;
    private HashMap<String, String> validationErrors;
    private Instant timestamp;

    public ValidationErrorResponse(String message, String error, Integer status, HashMap<String, String> validationErrors) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = Instant.now();
        this.validationErrors = validationErrors;
    }
}
