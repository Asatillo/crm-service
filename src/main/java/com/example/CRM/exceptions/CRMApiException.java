package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CRMApiException extends RuntimeException{

    private transient ApiResponse apiResponse;

    private HttpStatus status;


    public CRMApiException(String message) {
        super();
        this.status = HttpStatus.BAD_REQUEST;
        this.apiResponse = new ApiResponse(Boolean.FALSE, message);
    }

}
