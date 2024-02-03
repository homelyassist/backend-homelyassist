package com.homelyassist.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MemberRegistrationStatus {
    @JsonProperty("successful")
    SUCCESSFUL,
    @JsonProperty("error")
    ERROR
}
