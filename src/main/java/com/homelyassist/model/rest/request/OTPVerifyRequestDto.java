package com.homelyassist.model.rest.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerifyRequestDto {

    private String phoneNumber;
    private String otp;
}
