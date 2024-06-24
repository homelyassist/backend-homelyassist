package com.homelyassist.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AssistType {

    @JsonProperty("agriculture")
    AGRICULTURE,

    @JsonProperty("construction")
    CONSTRUCTION,

    @JsonProperty("electrical")
    ELECTRICAL
}
