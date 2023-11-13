package com.example.CRM.utils;

import com.example.CRM.exceptions.CRMApiException;
import com.example.CRM.model.Customer;

import java.util.Arrays;

public class AppUtils {
    public static void validatePageNumberAndSize(int page, int size, String sort) {
        if (page < 0) {
            throw new CRMApiException("Page number cannot be less than zero.");
        }

        if (size < 0) {
            throw new CRMApiException("Size number cannot be less than zero.");
        }

        if (size > AppConstants.MAX_PAGE_SIZE) {
            throw new CRMApiException(String.format("Size number cannot exceed %d", AppConstants.MAX_PAGE_SIZE));
        }

        if(Arrays.stream(Customer.class.getDeclaredFields()).noneMatch(field -> field.getName().equals(sort))){
            throw new CRMApiException(String.format("No property '%s' found", sort));
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
