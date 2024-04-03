package com.homelyassist.model.db;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.homelyassist.model.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class User {

    @JsonProperty("name")
    @Column(name = "name", nullable = false)
    private String name;

    @JsonProperty("gender")
    @Column(name = "gender", nullable = false)
    private Gender gender;

    @JsonProperty("phone_number")
    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @JsonProperty("address")
    @Column(name = "address", nullable = false)
    private String address;

    @JsonProperty("state")
    @Column(name = "state", nullable = false)
    private String state;

    @JsonProperty("district")
    @Column(name = "district", nullable = false)
    private String district;

    @JsonProperty("pin_code")
    @Column(name = "pin_code", nullable = false)
    private String pinCode;

    @JsonProperty("city_area")
    @Column(name = "city_area", nullable = false)
    private String cityArea;

    @JsonProperty("landmark")
    @Column(name = "landmark")
    private String landMark;

    @JsonProperty("active")
    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @JsonProperty("created")
    @Column(name = "created")
    private LocalDateTime created;

    @JsonProperty("modified")
    @Column(name = "modified")
    private LocalDateTime modified;

    @Lob
    @Column(name = "image", columnDefinition = "MEDIUMBLOB")
    private byte[] image;
}