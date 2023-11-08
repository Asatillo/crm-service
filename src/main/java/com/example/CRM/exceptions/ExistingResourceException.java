package com.example.CRM.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.CONFLICT)
public class ExistingResourceException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public ExistingResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' is already in use", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
