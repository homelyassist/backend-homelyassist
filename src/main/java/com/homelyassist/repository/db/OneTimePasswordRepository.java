package com.homelyassist.repository.db;

import com.homelyassist.model.db.OTPData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OTPData, Long> {

    @Query(value = "SELECT otp FROM OTPData otp WHERE otp.phoneNumber = :phoneNumber")
    OTPData findOtpByPhoneNumber(@Param("phoneNumber") String phoneNumber);

    @Modifying
    @Transactional
    @Query("DELETE FROM OTPData o WHERE o.expirationTime < ?1")
    void deleteExpiredOTP(LocalDateTime now);
}
