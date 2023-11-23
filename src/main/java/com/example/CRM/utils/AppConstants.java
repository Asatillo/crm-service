package com.example.CRM.utils;

import java.util.List;

public class AppConstants {

    public static final String API_URL = "/api";
    public static final int MAX_PAGE_SIZE = 50;
    public static final String DEFAULT_PAGE_NUMBER = "1";
    public static final String DEFAULT_PAGE_SIZE = "10";
    public static final String DEFAULT_SORT_PROPERTY = "id";

    public static final String REGEX_PHONE_NUMBER = "(36[237]0\\d{7})";

    /*
    * (25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}): Matches each octet, ensuring it's a valid number between 0 and 255.
    * 25[0-5]: Matches numbers 250 to 255.
    * 2[0-4][0-9]: Matches numbers 200 to 249.
    * [0-1]?[0-9]{1,2}: Matches numbers 0 to 199, allowing for single or double-digit numbers.
    * (\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})){3}: Matches the remaining three octets, separated by dots.
    * */
    public static final String REGEX_ROUTER_IDENTIFIER = "((25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})(\\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})){3})";

    public static final String DEVICE_TYPES_REGEX = "MOBILE|ROUTER";

    public static final String REGEX_NETWORK_IDENTIFIER = REGEX_PHONE_NUMBER + "|" + REGEX_ROUTER_IDENTIFIER;

    /*
     * The default quantity measures are:
     * Voice: Minutes
     * Data: GB
     * SMS: Number of SMS
     */
    public static final String SERVICE_TYPES_REGEX = "VOICE|DATA|SMS";

    public static final String SEGMENT_TYPES_REGEX = "PREMIUM|GOLD|SILVER|BRONZE|EXPLORE";

    public static final List<String> CITIES_WITH_WIRED_INTERNET = List.of("Budapest");
}
