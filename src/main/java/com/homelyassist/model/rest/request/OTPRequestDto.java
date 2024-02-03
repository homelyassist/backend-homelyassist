package com.homelyassist.model.rest.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTPRequestDto {

    private final String countryCode = "+91";
    private String phoneNumber;

    public String getPhoneNumberWithCountryCode() {
        return String.format("%s %s", countryCode, phoneNumber);
    }
}
