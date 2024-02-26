package com.homelyassist.model.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.AssistType;
import com.homelyassist.model.enums.OTPVerifyStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssistLoginResponseDto {

    @JsonProperty("status")
    private OTPVerifyStatus otpVerifyStatus;

    @JsonProperty("token")
    private String token;

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("type")
    private AssistType assistType;
}
