package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    private ApiResponse apiResponse;

    public BadRequestException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }

    public ApiResponse getApiResponse() {
        return apiResponse;
    }
}