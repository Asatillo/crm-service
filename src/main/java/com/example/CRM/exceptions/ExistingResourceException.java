package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ExistingResourceException extends RuntimeException{

    private final ApiResponse apiResponse;

    private final HttpStatus status = HttpStatus.CONFLICT;

    public ExistingResourceException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }
}
