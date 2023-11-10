package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = {CRMApiException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(CRMApiException exception){
        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(BadRequestException exception){
        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(ResourceNotFoundException exception){
        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InvalidInputException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(InvalidInputException exception){
        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ExistingResourceException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(ExistingResourceException exception){
        ApiResponse apiResponse = exception.getApiResponse();

        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(HttpRequestMethodNotSupportedException exception){
        String message = "Requested method '" + exception.getMethod() + "' is not allowed for this endpoint. " +
                "Allowed methods are: " + exception.getSupportedHttpMethods();

        return new ResponseEntity<>(new ExceptionResponse(message, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                HttpStatus.METHOD_NOT_ALLOWED.value()), HttpStatus.METHOD_NOT_ALLOWED);
    }
}
