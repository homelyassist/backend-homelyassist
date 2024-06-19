package com.homelyassist.service.otp;


import com.homelyassist.model.MSG91OtpData;
import com.homelyassist.model.db.OTPData;
import com.homelyassist.model.db.UserMapping;
import com.homelyassist.model.enums.OTPGenerateStatus;
import com.homelyassist.model.enums.OTPVerifyStatus;
import com.homelyassist.model.rest.request.AssistLoginRequestDto;
import com.homelyassist.model.rest.request.AssistResetPasswordRequestDto;
import com.homelyassist.model.rest.request.OTPRequestDto;
import com.homelyassist.model.rest.request.OTPVerifyRequestDto;
import com.homelyassist.model.rest.response.AnonymousTokenResponseDto;
import com.homelyassist.model.rest.response.AssistLoginResponseDto;
import com.homelyassist.model.rest.response.OTPResponseDto;
import com.homelyassist.model.rest.response.OTPVerifyResponseDto;
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

    public OTPResponseDto generateOTP(OTPRequestDto otpRequestDto, boolean isUiOtp) {
        String phoneNumber = otpRequestDto.getPhoneNumber();
        OTPData otpData = new OTPData();
        if (!BasicValidationHelper.isValidIndianPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }
        otpData.setPhoneNumber(phoneNumber);
        otpData.setCode(OTPHelper.createRandomOneTimePassword().get());
        otpData.setExpirationTime(LocalDateTime.now().plusMinutes(AppConstant.OTP.EXPIRY_INTERVAL));
        log.info("Sending opt with msg91 with Opt Data: {}", otpData);
        OTPResponseDto otpResponseDto = new OTPResponseDto();
        try {
            if (Boolean.FALSE == isUiOtp) {
                msg91OtpService.send(new MSG91OtpData(otpData.getPhoneNumberWithCountryCode(), new MSG91OtpData.OTP(otpData.getCode())));
            }
            oneTimePasswordRepository.save(otpData);
            if (Boolean.TRUE == isUiOtp) {
                otpResponseDto.setCode(otpData.getCode());
            }
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

//        if (Objects.nonNull(otpData)) {
//            oneTimePasswordRepository.delete(otpData);
//        }
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
            return assistLoginResponseDto;
        }

        String token = jwtService.generateToken(assistLoginRequestDto.getPhoneNumber());
        assistLoginResponseDto.setToken(token);
        assistLoginResponseDto.setUuid(userMapping.getId());
        assistLoginResponseDto.setAssistType(userMapping.getAssistType());
        assistLoginResponseDto.setOtpVerifyStatus(OTPVerifyStatus.SUCCESS);

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

    public void resetPassword(AssistResetPasswordRequestDto passwordRequestDto) {
        String phoneNumber = passwordRequestDto.getPhoneNumber();
        OTPVerifyResponseDto otpVerifyResponseDto = validateOTP(new OTPVerifyRequestDto(phoneNumber, passwordRequestDto.getOtp()));
        if (otpVerifyResponseDto.getOtpVerifyStatus().equals(OTPVerifyStatus.ERROR)) {
            throw new RuntimeException(otpVerifyResponseDto.getOtpVerifyStatus().toString());
        }
        UserMapping userMapping = userMappingRepository.findByPhoneNumber(phoneNumber);
        userMapping.setPassword(BCryptUtils.encodePassword(passwordRequestDto.getPassword()));
        userMappingRepository.save(userMapping);
    }

    public OTPResponseDto resetPasswordOtpGenerate(OTPRequestDto otpRequestDto) {
        UserMapping userMapping = userMappingRepository.findByPhoneNumber(otpRequestDto.getPhoneNumber());
        OTPResponseDto otpResponseDto = new OTPResponseDto();
        if (userMapping == null) {
            otpResponseDto.setOtpGenerateStatus(OTPGenerateStatus.FAILED);
            otpResponseDto.setError("Assist With Mobile No: " + otpRequestDto.getPhoneNumber() + " doesn't exist");
            return otpResponseDto;
        }

        return generateOTP(otpRequestDto, false);
    }
}
