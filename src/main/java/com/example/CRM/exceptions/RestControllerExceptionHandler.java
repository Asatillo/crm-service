package com.example.CRM.exceptions;

import com.example.CRM.payload.ApiResponse;
import com.example.CRM.payload.ExceptionResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;

@ControllerAdvice
public class RestControllerExceptionHandler {

    @ExceptionHandler(value = {ApiException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(ApiException exception){
        return new ResponseEntity<>(exception.getApiResponse(), exception.getStatus());
    }

    @ExceptionHandler(value = {BadRequestException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(BadRequestException exception){
        return new ResponseEntity<>(exception.getApiResponse(), exception.getStatus());
    }

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(ResourceNotFoundException exception){
        return new ResponseEntity<>(exception.getApiResponse(), exception.getStatus());
    }

    @ExceptionHandler(value = {InvalidInputException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(InvalidInputException exception){
        return new ResponseEntity<>(exception.getApiResponse(), exception.getStatus());
    }

    @ExceptionHandler(value = {ExistingResourceException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(ExistingResourceException exception){
        return new ResponseEntity<>(exception.getApiResponse(), exception.getStatus());
    }

    @ExceptionHandler(value = {ReferencedRecordException.class})
    @ResponseBody
    public ResponseEntity<ApiResponse> resolveException(ReferencedRecordException exception){
        return new ResponseEntity<>(exception.getApiResponse(), exception.getStatus());
    }

    @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(HttpRequestMethodNotSupportedException exception){
        String message = String.format("Requested method '%s' is not allowed for this endpoint. " +
                "Allowed methods are: %s", exception.getMethod(), exception.getSupportedHttpMethods());

        return new ResponseEntity<>(new ExceptionResponse(message, HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase(),
                HttpStatus.METHOD_NOT_ALLOWED.value()), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<ExceptionResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        HashMap<String, String> validationErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        return new ResponseEntity<>(
                new ExceptionResponse(validationErrors.size() == 1 ? "Validation error" : "Validation errors",
                        HttpStatus.BAD_REQUEST.getReasonPhrase(), HttpStatus.BAD_REQUEST.value(), validationErrors),
                HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(DataIntegrityViolationException exception){
        String message =  exception.getMostSpecificCause().getMessage();

        return new ResponseEntity<>(new ExceptionResponse(message, HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {MissingServletRequestParameterException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(MissingServletRequestParameterException exception){

        return new ResponseEntity<>(new ExceptionResponse("Required request parameter for method is not present", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    @ResponseBody
    public ResponseEntity<ExceptionResponse> resolveException(HttpMessageNotReadableException exception){

        return new ResponseEntity<>(new ExceptionResponse("Required request body is missing", HttpStatus.BAD_REQUEST.getReasonPhrase(),
                HttpStatus.BAD_REQUEST.value()), HttpStatus.BAD_REQUEST);
    }
}
