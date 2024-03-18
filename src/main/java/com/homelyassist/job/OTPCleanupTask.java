package com.homelyassist.job;

import com.homelyassist.repository.db.OneTimePasswordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class OTPCleanupTask {

    @Autowired
    private OneTimePasswordRepository otpDataRepository;

    @Scheduled(fixedRate = 3600000)
    public void cleanExpiredOTP() {
        LocalDateTime now = LocalDateTime.now();
        otpDataRepository.deleteExpiredOTP(now);
    }
}
