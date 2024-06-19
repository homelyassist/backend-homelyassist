package com.homelyassist.model.rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.OTPGenerateStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public class OTPResponseDto {

    @JsonProperty("code")
    private String code;

    @JsonProperty("status")
    private OTPGenerateStatus otpGenerateStatus;

    @JsonProperty("error")
    private String error;
}
