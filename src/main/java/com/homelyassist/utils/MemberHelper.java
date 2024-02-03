package com.homelyassist.utils;

import com.homelyassist.exception.MemberRegistrationValidationFailed;
import com.homelyassist.model.db.User;

import java.util.Objects;

public class MemberHelper {

    public static void validateDefaultProperties(User user) {

        if(Objects.isNull(user)) {
            throw new MemberRegistrationValidationFailed();
        }

        String name = user.getName();
        String phoneNumber = user.getPhoneNumber();
        String address = user.getAddress();
        String state = user.getState();
        String district = user.getDistrict();
        String pinCode = user.getPinCode();
        String cityArea = user.getCityArea();

        if(Objects.isNull(name) || name.isBlank()) {
            throw new MemberRegistrationValidationFailed("name");
        }

        if(Objects.isNull(phoneNumber) || phoneNumber.isBlank()) {
            throw new MemberRegistrationValidationFailed("phone_number");
        }

        if(!BasicValidationHelper.isValidIndianPhoneNumber(phoneNumber)) {
            throw new MemberRegistrationValidationFailed("phone_number", phoneNumber);
        }

        if(Objects.isNull(address) || address.isBlank()) {
            throw new MemberRegistrationValidationFailed("address");
        }

        if(Objects.isNull(state) || state.isBlank()) {
            throw new MemberRegistrationValidationFailed("state");
        }

        if(Objects.isNull(district) || district.isBlank()) {
            throw new MemberRegistrationValidationFailed("district");
        }

        if(Objects.isNull(pinCode) || !BasicValidationHelper.isValidPinCode(pinCode)) {
            throw new MemberRegistrationValidationFailed("pin_code", pinCode);
        }

        if(Objects.isNull(cityArea) || cityArea.isBlank() ) {
            throw new MemberRegistrationValidationFailed("city_area");
        }
    }
}
