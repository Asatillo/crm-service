package com.example.CRM.utils;

public class AppConstants {
    public static final int MAX_PAGE_SIZE = 50;
    public static final String DEFAULT_PAGE_NUMBER = "0";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_PROPERTY = "id";

    public static final String REGEX_PHONE_NUMBER = "(36|06)[237]0\\d{7}";

    /*
     * The default quantity measures are:
     * Voice: Minutes
     * Data: GB
     * SMS: Number of SMS
     */
    public static final String PACKAGE_TYPES_REGEX = "VOICE|DATA|SMS";

    public static final String SEGMENT_TYPES_REGEX = "PREMIUM|GOLD|SILVER|BRONZE|EXPLORE";
}
