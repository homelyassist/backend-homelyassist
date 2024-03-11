package com.homelyassist.service.otp;


import com.homelyassist.model.db.OTPData;
import com.homelyassist.model.db.UserMapping;
import com.homelyassist.model.enums.OTPGenerateStatus;
import com.homelyassist.model.rest.request.OTPRequestDto;
import com.homelyassist.model.rest.response.AssistLoginResponseDto;
import com.homelyassist.model.rest.response.OTPResponseDto;
import com.homelyassist.model.rest.request.OTPVerifyRequestDto;
import com.homelyassist.model.rest.response.OTPVerifyResponseDto;
import com.homelyassist.model.enums.OTPVerifyStatus;
import com.homelyassist.repository.db.OneTimePasswordRepository;
import com.homelyassist.repository.db.UserMappingRepository;
import com.homelyassist.service.jwt.JWTService;
import com.homelyassist.utils.AppConstant;
import com.homelyassist.utils.BasicValidationHelper;
import com.homelyassist.utils.OTPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
@Slf4j
public class OTPService {

    private final OneTimePasswordRepository oneTimePasswordRepository;
    private final UserMappingRepository userMappingRepository;
    private final TwilioService twilioService;
    private final JWTService jwtService;

    @Autowired
    public OTPService(OneTimePasswordRepository oneTimePasswordRepository, UserMappingRepository userMappingRepository, TwilioService twilioService, JWTService jwtService) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
        this.userMappingRepository = userMappingRepository;
        this.twilioService = twilioService;
        this.jwtService = jwtService;
    }

    public OTPResponseDto generateOTP(OTPRequestDto otpRequestDto) {
        String phoneNumber = otpRequestDto.getPhoneNumber();
        OTPData otpData = new OTPData();
        if(!BasicValidationHelper.isValidIndianPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        otpData.setPhoneNumber(phoneNumber);
        otpData.setCode(OTPHelper.createRandomOneTimePassword().get());
        otpData.setExpirationTime(LocalDateTime.now().plusMinutes(AppConstant.OTP.EXPIRY_INTERVAL));
        log.info("Sending opt with twilio with Opt Data: {}", otpData);
        OTPResponseDto otpResponseDto = new OTPResponseDto();
        try {
            // TODO: uncomment this once we've premium account
            //twilioService.sendOtp(new TwilioOtpData(otpData.getPhoneNumberWithCountryCode(), "Testing otp: " + otpData.getCode()));
            oneTimePasswordRepository.save(otpData);
            otpResponseDto.setOtpGenerateStatus(OTPGenerateStatus.COMPLETED);
        } catch (Exception e) {
            log.error("error processing otp generation", e);
            otpResponseDto.setOtpGenerateStatus(OTPGenerateStatus.FAILED);
        }

        return otpResponseDto;
    }

    public OTPVerifyResponseDto validateOTP(OTPVerifyRequestDto otpVerifyDto) {
        OTPData otpData = oneTimePasswordRepository.findOtpByPhoneNumber(otpVerifyDto.getPhoneNumber());
        OTPVerifyResponseDto otpVerifyResponseDto = new OTPVerifyResponseDto();
        if (Objects.nonNull(otpData) && !otpData.isExpired() && otpData.getCode().equals(otpVerifyDto.getOtp())) {
            log.info("OTP verification successful");
            String token = jwtService.generateToken(otpData.getPhoneNumber());
            otpVerifyResponseDto.setToken(token);
            otpVerifyResponseDto.setOtpVerifyStatus(OTPVerifyStatus.SUCCESS);
        } else {
            log.info("Invalid OTP");
            otpVerifyResponseDto.setOtpVerifyStatus(OTPVerifyStatus.ERROR);
        }

        if (Objects.nonNull(otpData)) {
            oneTimePasswordRepository.delete(otpData);
        }
        return otpVerifyResponseDto;
    }

    public AssistLoginResponseDto login(OTPVerifyRequestDto otpVerifyDto) {
        String phoneNumber = otpVerifyDto.getPhoneNumber();
        OTPData otpData = oneTimePasswordRepository.findOtpByPhoneNumber(phoneNumber);
        AssistLoginResponseDto assistLoginResponseDto = new AssistLoginResponseDto();
        if (Objects.nonNull(otpData) && !otpData.isExpired() && otpData.getCode().equals(otpVerifyDto.getOtp())) {
            log.info("OTP verification successful");
            String token = jwtService.generateToken(otpData.getPhoneNumber());
            assistLoginResponseDto.setToken(token);
            UserMapping userMapping = userMappingRepository.findByPhoneNumber(phoneNumber);
            assistLoginResponseDto.setUuid(userMapping.getId());
            assistLoginResponseDto.setAssistType(userMapping.getAssistType());
            assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.SUCCESS);
        } else {
            log.info("Invalid OTP");
            assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.ERROR);
        }

        if (Objects.nonNull(otpData)) {
            oneTimePasswordRepository.delete(otpData);
        }
        return assistLoginResponseDto;
    }
}
