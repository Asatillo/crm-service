package com.example.CRM.utils;

import com.example.CRM.exceptions.CRMApiException;

import java.time.LocalDate;
import java.util.Arrays;

public class AppUtils {

    // Pagination and sorting validation methods
    public static void validatePaginationRequestParams(int page, int size, String sort, Class<?> clazz){
        validatePageNumberAndSize(page, size);
        validateSortFieldExists(sort, clazz);
    }

    public static void validatePageNumberAndSize(int page, int size) {
        validatePageNumberGreaterThanZero(page);
        validateSizeNumberGreaterThanZero(size);
        validatePageSizeLessThanMaxPageSize(size);
    }

    public static void validatePageNumberGreaterThanZero(int page){
        if(page < 0){
            throw new CRMApiException("Page number must be greater than zero.");
        }
    }

    public static void validateSizeNumberGreaterThanZero(int size){
        if(size < 0){
            throw new CRMApiException("Size number must be greater than zero.");
        }
    }

    public static void validatePageSizeLessThanMaxPageSize(int size) {
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new CRMApiException(String.format("Size number cannot exceed %d", AppConstants.MAX_PAGE_SIZE));
        }
    }

    /* Checks if the requested page number is less than the total number of pages */
    public static void validatePageNumberLessThanTotalPages(int page, int totalPages) {
        if (page > totalPages) {
            throw new CRMApiException(String.format("Requested page does not exist. Total pages: %d", totalPages+1));
        }
    }

    // Input data validation methods
    public static void validateSortFieldExists(String sort, Class<?> clazz){
        if(Arrays.stream(clazz.getDeclaredFields()).noneMatch(field -> field.getName().equals(sort))){
            throw new CRMApiException(String.format("No property '%s' found", sort));
        }
    }

    public static boolean isValid(String regex, String input) {
        return input.matches(regex);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return isValid(AppConstants.REGEX_PHONE_NUMBER, phoneNumber);
    }

    public static boolean isOlderThan18(LocalDate dob) {
        return dob.plusYears(18).isBefore(LocalDate.now());
    }

    // TODO: implement
    public static boolean isValidCity(String city) {
        return true;
    }
}
