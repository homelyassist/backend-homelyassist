package com.homelyassist.model.rest.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssistDetailRequestDTO {

    @JsonProperty("vid")
    private String vid;

    @JsonProperty("assist_id")
    private String assistId;
}
