package com.homelyassist.model.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchAssistRequestDto<T> {

    @JsonProperty("state")
    private String state;

    @JsonProperty("district")
    private String district;

    @JsonProperty("block")
    private String block;

    @JsonProperty("village")
    private String village;

    @JsonProperty("assist_types")
    private List<T> assistTypes;
}
