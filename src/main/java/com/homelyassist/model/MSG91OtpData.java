package com.homelyassist.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MSG91OtpData {

    private String phoneNumber;
    private OTP opt;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OTP {
        @JsonProperty("OTP")
        private String opt;
    }
}