package com.homelyassist.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AssistRegistrationStatus {
    @JsonProperty("successful")
    SUCCESSFUL,
    @JsonProperty("error")
    ERROR
}
