package com.homelyassist.utils;

public class AppConstant {

    public static class OTP {
        public static final Long EXPIRY_INTERVAL = 5L; // 5 minute
    }

    public static class ERROR {
        public static final String INTERNAL_SERVER_ERROR = "Internal server error";
    }

    public static class HTMLPage {
        public static final String ASSIST_REGISTER = "assist_register";
        public static final String HOME_PAGE = "index";

        public static final String ASSIST_AVAILABILITY = "assist_availability";
    }

    public static final String VALID_TOKEN = "OK";
}
