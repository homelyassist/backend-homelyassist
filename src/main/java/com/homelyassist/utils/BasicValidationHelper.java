package com.homelyassist.utils;

import java.util.regex.Pattern;

public class BasicValidationHelper {

    private static final String PIN_CODE_PATTERN = "^[1-9][0-9]{5}$";
    private static final String PHONE_NUMBER_PATTERN = "^[789]\\d{9}$";

    public static boolean isValidPinCode(String pinCode) {
        return Pattern.matches(PIN_CODE_PATTERN, pinCode);
    }

    public static boolean isValidIndianPhoneNumber(String phoneNumber) {
        return Pattern.matches(PHONE_NUMBER_PATTERN, phoneNumber);
    }
}
