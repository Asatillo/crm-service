package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException{

    private final transient ApiResponse apiResponse;

    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public InvalidInputException(String resourceName, String fieldName, Object fieldValue) {
        super();
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.apiResponse = new ApiResponse(false, String.format("%s with %s value '%s' doesn't match the pattern", resourceName, fieldName, fieldValue));
    }

    public InvalidInputException(ApiResponse apiResponse) {
        super();
        this.apiResponse = apiResponse;
    }

}
