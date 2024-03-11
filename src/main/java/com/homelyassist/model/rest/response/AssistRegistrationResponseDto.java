package com.homelyassist.model.rest.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.AssistRegistrationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AssistRegistrationResponseDto {

    @JsonProperty("uuid")
    private String uuid;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("status")
    private AssistRegistrationStatus assistRegistrationStatus;

    @JsonProperty("error")
    private String errorMessage;
}
