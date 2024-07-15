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
        public static final String SEARCH_ASSIST = "search_assist";

        public static final String ASSIST_LOGIN = "assist_login";

        public static final String ASSIST_FORGOT_PASSWORD = "forgot_password";

        public static final String ASSIST_OTP_VERIFICATION = "otp_verification";

        public static final String SEARCH_AGRICULTURE_ASSIST = "search_agriculture_assist";

        public static final String SEARCH_CONSTRUCTION_ASSIST = "search_construction_assist";

        public static final String SEARCH_ELECTRICAL_ASSIST = "search_electrical_assist";

        public static final String ASSIST = "assist";
    }

    public static final String VALID_TOKEN = "OK";
}
