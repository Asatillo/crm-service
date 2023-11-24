package com.example.CRM.utils;

import com.example.CRM.exceptions.CRMApiException;
import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.payload.ApiResponse;

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

    public static void validateDeviceType(String deviceType) {
        if(!deviceType.equals("MOBILE") && !deviceType.equals("ROUTER")){
            throw new CRMApiException(String.format("Device type must be one of the following: %s", AppConstants.DEVICE_TYPES_REGEX));
        }
    }

    public static void validateDOB(LocalDate dob) {
        if (dob.plusYears(18).isBefore(LocalDate.now())){
            throw new InvalidInputException(new ApiResponse(false, "Customer must be older than 18"));
        }
    }
}
