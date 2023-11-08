package com.example.CRM.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidInputException extends RuntimeException{
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    public InvalidInputException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s with %s '%s' is invalid", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }
}
