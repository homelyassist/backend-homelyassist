package com.homelyassist.model.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.OTPVerifyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OTPVerifyResponseDto {

    @JsonProperty("status")
    private OTPVerifyStatus otpVerifyStatus;

    @JsonProperty("token")
    private String token;
}
