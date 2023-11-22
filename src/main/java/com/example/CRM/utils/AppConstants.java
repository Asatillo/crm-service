package com.example.CRM.utils;

public class AppConstants {

    public static final String API_URL = "/api";
    public static final int MAX_PAGE_SIZE = 50;
    public static final String DEFAULT_PAGE_NUMBER = "1";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_PROPERTY = "id";

    public static final String REGEX_PHONE_NUMBER = "(36[237]0\\d{7})";


    /*
     * The default quantity measures are:
     * Voice: Minutes
     * Data: GB
     * SMS: Number of SMS
     */
    public static final String SERVICE_TYPES_REGEX = "VOICE|DATA|SMS";

    public static final String DEVICE_TYPES_REGEX = "MOBILE|ROUTER";

    public static final String SEGMENT_TYPES_REGEX = "PREMIUM|GOLD|SILVER|BRONZE|EXPLORE";
}
