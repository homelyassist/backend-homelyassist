package com.homelyassist.repository.db;

import com.homelyassist.model.db.OTPData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OneTimePasswordRepository extends JpaRepository<OTPData, Long> {

    @Query(value = "SELECT otp FROM OTPData otp WHERE otp.phoneNumber = :phoneNumber")
    OTPData findOtpByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
