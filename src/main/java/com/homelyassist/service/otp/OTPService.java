package com.homelyassist.service.otp;


import com.homelyassist.model.MSG91OtpData;
import com.homelyassist.model.db.OTPData;
import com.homelyassist.model.db.UserMapping;
import com.homelyassist.model.enums.OTPGenerateStatus;
import com.homelyassist.model.rest.request.AssistLoginRequestDto;
import com.homelyassist.model.rest.request.OTPRequestDto;
import com.homelyassist.model.rest.response.AnonymousTokenResponseDto;
import com.homelyassist.model.rest.response.AssistLoginResponseDto;
import com.homelyassist.model.rest.response.OTPResponseDto;
import com.homelyassist.model.rest.request.OTPVerifyRequestDto;
import com.homelyassist.model.rest.response.OTPVerifyResponseDto;
import com.homelyassist.model.enums.OTPVerifyStatus;
import com.homelyassist.repository.db.OneTimePasswordRepository;
import com.homelyassist.repository.db.UserMappingRepository;
import com.homelyassist.service.jwt.JWTService;
import com.homelyassist.utils.AppConstant;
import com.homelyassist.utils.BCryptUtils;
import com.homelyassist.utils.BasicValidationHelper;
import com.homelyassist.utils.OTPHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class OTPService {

    private final OneTimePasswordRepository oneTimePasswordRepository;
    private final UserMappingRepository userMappingRepository;
    private final JWTService jwtService;

    private final MSG91OtpService msg91OtpService;

    @Autowired
    public OTPService(OneTimePasswordRepository oneTimePasswordRepository, UserMappingRepository userMappingRepository, JWTService jwtService, MSG91OtpService msg91OtpService) {
        this.oneTimePasswordRepository = oneTimePasswordRepository;
        this.userMappingRepository = userMappingRepository;
        this.jwtService = jwtService;
        this.msg91OtpService = msg91OtpService;
    }

    public OTPResponseDto generateOTP(OTPRequestDto otpRequestDto) {
        String phoneNumber = otpRequestDto.getPhoneNumber();
        OTPData otpData = new OTPData();
        if (!BasicValidationHelper.isValidIndianPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        otpData.setPhoneNumber(phoneNumber);
        otpData.setCode(OTPHelper.createRandomOneTimePassword().get());
        otpData.setExpirationTime(LocalDateTime.now().plusMinutes(AppConstant.OTP.EXPIRY_INTERVAL));
        log.info("Sending opt with twilio with Opt Data: {}", otpData);
        OTPResponseDto otpResponseDto = new OTPResponseDto();
        try {
            // TODO: uncomment this once we've premium account
            //msg91OtpService.send(new MSG91OtpData(otpData.getPhoneNumberWithCountryCode(), new MSG91OtpData.OTP(otpData.getCode())));
            //twilioService.sendOtp(new TwilioOtpData(otpData.getPhoneNumberWithCountryCode(), "Testing otp: " + otpData.getCode()));
            oneTimePasswordRepository.save(otpData);
            otpResponseDto.setCode(otpData.getCode());
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

    public AssistLoginResponseDto login(AssistLoginRequestDto assistLoginRequestDto) {
        String phoneNumber = assistLoginRequestDto.getPhoneNumber();
        UserMapping userMapping = userMappingRepository.findByPhoneNumber(phoneNumber);
        AssistLoginResponseDto assistLoginResponseDto = new AssistLoginResponseDto();
        if (userMapping == null) {
            assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.ERROR);
            assistLoginResponseDto.setError("User doesn't exist");
            return assistLoginResponseDto;
        }

        if (!BCryptUtils.matchPassword(assistLoginRequestDto.getPassword(), userMapping.getPassword())) {
            assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.ERROR);
            assistLoginResponseDto.setError("Incorrect password. Please try again.");
        }

        String token = jwtService.generateToken(assistLoginRequestDto.getPhoneNumber());
        assistLoginResponseDto.setToken(token);
        assistLoginResponseDto.setUuid(userMapping.getId());
        assistLoginResponseDto.setAssistType(userMapping.getAssistType());
        assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.SUCCESS);

//        if (Objects.nonNull(otpData) && !otpData.isExpired() && otpData.getCode().equals(otpVerifyDto.getOtp())) {
//            log.info("OTP verification successful");
//            String token = jwtService.generateToken(otpData.getPhoneNumber());
//            assistLoginResponseDto.setToken(token);
//            UserMapping userMapping = userMappingRepository.findByPhoneNumber(phoneNumber);
//            if (userMapping == null) {
//                assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.ERROR);
//                assistLoginResponseDto.setError("User doesn't exist");
//            } else {
//                assistLoginResponseDto.setUuid(userMapping.getId());
//                assistLoginResponseDto.setAssistType(userMapping.getAssistType());
//                assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.SUCCESS);
//            }
//        } else {
//            log.info("Invalid OTP");
//            assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.ERROR);
//            assistLoginResponseDto.setError("Invalid OTP");
//        }


//        if (Objects.nonNull(otpData)) {
//            oneTimePasswordRepository.delete(otpData);
//        }

        return assistLoginResponseDto;
    }

    public AnonymousTokenResponseDto generateAnonymousToken(OTPVerifyRequestDto otpVerifyDto) {
        String phoneNumber = otpVerifyDto.getPhoneNumber();
        OTPData otpData = oneTimePasswordRepository.findOtpByPhoneNumber(phoneNumber);
        AnonymousTokenResponseDto anonymousTokenResponseDto = new AnonymousTokenResponseDto();
        anonymousTokenResponseDto.setPhoneNumber(phoneNumber);
        anonymousTokenResponseDto.setVid(UUID.randomUUID().toString());
        if (Objects.nonNull(otpData) && !otpData.isExpired() && otpData.getCode().equals(otpVerifyDto.getOtp())) {
            log.info("OTP verification successful");
            String token = jwtService.generateToken(otpData.getPhoneNumber());
            anonymousTokenResponseDto.setToken(token);
            anonymousTokenResponseDto.setExpiry(LocalDateTime.now().plusMinutes(30));
            anonymousTokenResponseDto.setOtpVerifyStatus(OTPVerifyStatus.SUCCESS);
        } else {
            log.info("Invalid OTP");
            anonymousTokenResponseDto.setOtpVerifyStatus(OTPVerifyStatus.ERROR);
        }

        if (Objects.nonNull(otpData)) {
            oneTimePasswordRepository.delete(otpData);
        }
        return anonymousTokenResponseDto;
    }
}
