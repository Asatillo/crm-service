package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private transient ApiResponse apiResponse;
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super();
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
        this.apiResponse = new ApiResponse(Boolean.FALSE, String.format("%s with %s '%s' not found", resourceName, fieldName, fieldValue));
    }
}