package com.homelyassist.model.db;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
public class OTPData {

    @Id
    @NonNull
    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @NonNull
    @Column(name = "code", nullable = false)
    private String code;

    @NonNull
    @Column(name = "expiration_time", nullable = false)
    private LocalDateTime expirationTime;


    public Boolean isExpired() {
        return LocalDateTime.now().isAfter(expirationTime);
    }

    public String getPhoneNumberWithCountryCode() {
        return "91" + phoneNumber;
    }
}
