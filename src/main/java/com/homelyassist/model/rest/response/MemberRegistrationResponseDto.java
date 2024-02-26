package com.homelyassist.model.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.MemberRegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MemberRegistrationResponseDto {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("status")
    private MemberRegistrationStatus memberRegistrationStatus;

    @JsonProperty("error")
    private String errorMessage;
}
