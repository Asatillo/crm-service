package com.example.CRM.utils;

import org.springframework.http.HttpStatus;
import com.example.CRM.exceptions.CRMApiException;

public class AppUtils {
    public static void validatePageNumberAndSize(int page, int size) {
        if (page < 0) {
            throw new CRMApiException(HttpStatus.BAD_REQUEST, "Page number cannot be less than zero.");
        }

        if (size < 0) {
            throw new CRMApiException(HttpStatus.BAD_REQUEST, "Size number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new CRMApiException(HttpStatus.BAD_REQUEST, "Page size must not be greater than " + AppConstants.MAX_PAGE_SIZE);
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