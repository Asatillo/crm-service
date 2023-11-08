package com.example.CRM.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

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

    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
