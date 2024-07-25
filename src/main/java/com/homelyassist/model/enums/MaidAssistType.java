package com.homelyassist.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MaidAssistType {
    @JsonProperty("cook")
    COOK,

    @JsonProperty("maid")
    MAID,

    @JsonProperty("tiffin_service")
    TIFFIN_SERVICE
}
