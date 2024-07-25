package com.homelyassist.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(indexes = { @Index(name = "phone_number", columnList = "phone_number")})
public class ElectricalAssist extends User {

    @Id
    @JsonProperty("id")
    @Column(name = "id")
    private String id;

    @JsonProperty("experience")
    @Column(name = "experience")
    private Integer yearOfExperience;

    @JsonProperty("description")
    @Column(name = "description")
    private String description;
}
