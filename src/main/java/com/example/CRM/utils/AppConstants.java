package com.example.CRM.utils;

public class AppConstants {
    public static final int MAX_PAGE_SIZE = 50;
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_PROPERTY = "id";

    public static final String REGEX_PHONE_NUMBER = "(\\+36|06)[\\s-]?[237]0[\\s-]?\\d{7}";

    // TODO: optimize regex to not start with special characters
    public static final String REGEX_EMAIL = "^[\\w\\-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
}