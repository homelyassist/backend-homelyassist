package com.homelyassist.model.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.AgriculturalAssistType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchAssistRequestDto {

    @JsonProperty("assist_types")
    private List<AgriculturalAssistType> assistTypes;
}
