package com.homelyassist.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum ConstructionAssistType {
    @JsonProperty("home_construction")
    HOME_CONSTRUCTION,

    @JsonProperty("home_repair")
    HOME_REPAIR,

    @JsonProperty("carpenter")
    CARPENTER,

    @JsonProperty("plumber")
    PLUMBER,

    @JsonProperty("painting")
    PAINTING,

    @JsonProperty("fitting")
    FITTING
}
