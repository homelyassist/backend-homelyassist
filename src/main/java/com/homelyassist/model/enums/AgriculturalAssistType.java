package com.homelyassist.model.enums;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum AgriculturalAssistType {
    @JsonProperty("agriculture")
    AGRICULTURE,

    @JsonProperty("farm_land_maintenance")
    FARM_LAND_MAINTENANCE,

    @JsonProperty("wood_cutting")
    WOOD_CUTTING,

    @JsonProperty("tractor_power_tiller")
    TRACTOR_POWER_TILLER,

    @JsonProperty("animal_selling")
    ANIMAL_SELLING
}
