package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiException extends RuntimeException{

    private final transient ApiResponse apiResponse;

    private final HttpStatus status;


    public ApiException(String message) {
        super();
        this.status = HttpStatus.BAD_REQUEST;
        this.apiResponse = new ApiResponse(false, message);
    }

}
