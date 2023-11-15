package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ExistingResourceException extends RuntimeException{

    private ApiResponse apiResponse;

    public ExistingResourceException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }
}
