package com.example.CRM.payload;

import lombok.Data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class ExceptionResponse {
    private String error;
    private Integer status;
    private String message;
    private Instant timestamp;

    public ExceptionResponse(String message, String error, Integer status) {
        this.message = message;
        this.error = error;
        this.status = status;
        this.timestamp = Instant.now();
    }
}