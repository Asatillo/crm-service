package com.example.CRM.utils;

import com.example.CRM.exceptions.ApiException;
import com.example.CRM.exceptions.InvalidInputException;
import com.example.CRM.payload.ApiResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.example.CRM.utils.AppConstants.MAJORITY_AGE;

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
            throw new ApiException("Page number must be greater than zero.");
        }
    }

    public static void validateSizeNumberGreaterThanZero(int size){
        if(size < 0){
            throw new ApiException("Size number must be greater than zero.");
        }
    }

    public static void validatePageSizeLessThanMaxPageSize(int size) {
        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new ApiException(String.format("Size number cannot exceed %d", AppConstants.MAX_PAGE_SIZE));
        }
    }

    /* Checks if the requested page number is less than the total number of pages */
    public static void validatePageNumberLessThanTotalPages(int page, int totalPages, long totalElements) {
        if (totalElements > 0 && page >= totalPages) {
            throw new ApiException(String.format("Requested page does not exist. Total pages: %d", totalPages));
        }
    }

    // Input data validation methods
    public static void validateSortFieldExists(String sort, Class<?> clazz){
        if(Arrays.stream(clazz.getDeclaredFields()).noneMatch(field -> field.getName().equals(sort))){
            throw new ApiException(String.format("No property '%s' found", sort));
        }
    }

    public static void validateDeviceType(String deviceType) {
        if(!deviceType.equals("MOBILE") && !deviceType.equals("ROUTER")){
            throw new ApiException(String.format("Device type must be one of the following: %s", AppConstants.DEVICE_TYPES_REGEX));
        }
    }

    public static void validateDOB(LocalDate dob) {
        if (!dob.plusYears(MAJORITY_AGE).isBefore(LocalDate.now())){
            throw new InvalidInputException(new ApiResponse(false, "Customer must be older than 18"));
        }
    }

    public static LocalDateTime getCurrentTime() {
        return LocalDateTime.now();
    }
}
