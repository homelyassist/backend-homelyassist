package com.homelyassist.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.AssistType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(indexes = { @Index(name = "phone_number", columnList = "phone_number")})
public class UserMapping {

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    private String id;

    @JsonProperty("phone_number")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @JsonProperty("password")
    @Column(name = "password", nullable = false)
    private String password;

    @JsonProperty("assist_type")
    @Column(name = "assist_type", nullable = false)
    private AssistType assistType;
}
