package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private final transient ApiResponse apiResponse;

    private final HttpStatus status = HttpStatus.NOT_FOUND;

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super();
        this.apiResponse = new ApiResponse(false, String.format("%s with %s '%s' not found", resourceName, fieldName, fieldValue));
    }
}