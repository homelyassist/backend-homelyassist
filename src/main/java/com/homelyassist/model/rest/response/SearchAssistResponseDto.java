package com.homelyassist.model.rest.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.db.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchAssistResponseDto<T extends User> {

    @JsonProperty("assist")
    private List<T> assist;
}
