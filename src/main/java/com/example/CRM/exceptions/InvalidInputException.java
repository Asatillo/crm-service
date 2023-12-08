package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException{

    private final transient ApiResponse apiResponse;

    private final HttpStatus status = HttpStatus.BAD_REQUEST;

    public InvalidInputException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }
}