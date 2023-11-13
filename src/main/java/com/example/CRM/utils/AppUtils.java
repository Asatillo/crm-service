package com.example.CRM.utils;

import com.example.CRM.exceptions.CRMApiException;
import com.example.CRM.model.Customer;

import java.util.Arrays;

public class AppUtils {
    public static void validatePageNumberAndSize(int page, int size, String sort) {
        validatePageNumberGreaterThanZero(page);
        validateSizeNumberGreaterThanZero(size);
        validatePageSizeLessThanMaxPageSize(size);
    }

    public static void validatePageNumberLessThanTotalPages(int page, int totalPages) {
        if (page > totalPages) {
            throw new CRMApiException(String.format("Requested page does not exist. Total pages: %d", totalPages+1));
        }
    }

    public static void validateSortFieldExists(String sort, Class<?> clazz){
        if(Arrays.stream(clazz.getDeclaredFields()).noneMatch(field -> field.getName().equals(sort))){
            throw new CRMApiException(String.format("No property '%s' found", sort));
        }
    }

    public static void validatePageSizeLessThanMaxPageSize(int size) {
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new CRMApiException(String.format("Size number cannot exceed %d", AppConstants.MAX_PAGE_SIZE));
        }
    }

    public static void validateSizeNumberGreaterThanZero(int size){
        if(size < 0){
            throw new CRMApiException("Size number must be greater than zero.");
        }
    }

    public static void validatePageNumberGreaterThanZero(int page){
        if(page < 0){
            throw new CRMApiException("Page number must be greater than zero.");
        }
    }

    public static boolean isValid(String regex, String input) {
        return input.matches(regex);
    }

    public static boolean isValidEmail(String email) {
        return isValid(AppConstants.REGEX_EMAIL, email);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return isValid(AppConstants.REGEX_PHONE_NUMBER, phoneNumber);
    }
}
