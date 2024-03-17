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
public class AssistDetailResponseDto {

    @JsonProperty("name")
    private String name;

    @JsonProperty("phone_number")
    private String phoneNumber;

}
