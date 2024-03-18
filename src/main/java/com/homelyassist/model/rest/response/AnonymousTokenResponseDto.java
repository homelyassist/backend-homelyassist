package com.homelyassist.model.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.OTPVerifyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AnonymousTokenResponseDto {

    @JsonProperty("vid")
    private String vid;

    @JsonProperty("status")
    private OTPVerifyStatus otpVerifyStatus;

    @JsonProperty("token")
    private String token;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("expiry")
    private LocalDateTime expiry;
}
