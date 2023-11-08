package com.example.CRM.exceptions;

import org.springframework.http.HttpStatus;

public class CRMApiException extends RuntimeException{
    private final HttpStatus status;
    private final String message;

    public CRMApiException(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
    }

    public CRMApiException(HttpStatus status, String message, Throwable exception) {
        super(exception);
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

}
