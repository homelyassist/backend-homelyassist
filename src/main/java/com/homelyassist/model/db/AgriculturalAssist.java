package com.homelyassist.model.db;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.AgriculturalAssistType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(indexes = { @Index(name = "phone_number", columnList = "phone_number")})
public class AgriculturalAssist extends User {

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

    @ElementCollection(targetClass = AgriculturalAssistType.class)
    @Enumerated(EnumType.STRING)
    @JsonProperty("assist_types")
    @Column(name = "assist_types", nullable = false, length=512)
    private List<AgriculturalAssistType> agriculturalAssistTypes;
}
