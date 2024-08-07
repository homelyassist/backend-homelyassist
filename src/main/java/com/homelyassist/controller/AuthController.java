package com.homelyassist.controller;

import com.homelyassist.model.rest.request.AssistLoginRequestDto;
import com.homelyassist.model.rest.request.AssistResetPasswordRequestDto;
import com.homelyassist.model.rest.request.OTPRequestDto;
import com.homelyassist.model.rest.request.OTPVerifyRequestDto;
import com.homelyassist.model.rest.response.AnonymousTokenResponseDto;
import com.homelyassist.model.rest.response.AssistLoginResponseDto;
import com.homelyassist.model.rest.response.OTPResponseDto;
import com.homelyassist.model.rest.response.OTPVerifyResponseDto;
import com.homelyassist.service.otp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.homelyassist.utils.AppConstant.VALID_TOKEN;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OTPService otpService;

    @Autowired
    public AuthController(OTPService otpService) {
        this.otpService = otpService;
    }

    @PostMapping("/otp/generate")
    public OTPResponseDto generateOtp(@RequestBody OTPRequestDto otpRequestDto) {
        return otpService.generateOTP(otpRequestDto, false);
    }

    @PostMapping("/otp/generate/ui")
    public OTPResponseDto generateOtpForUI(@RequestBody OTPRequestDto otpRequestDto) {
        return otpService.generateOTP(otpRequestDto, true);
    }

    @PostMapping("/otp/verify")
    public OTPVerifyResponseDto verifyOtp(@RequestBody OTPVerifyRequestDto otpVerifyDto) {
        return otpService.validateOTP(otpVerifyDto);
    }

    @PostMapping("/otp/generate/reset-password")
    public OTPResponseDto resetPasswordOtpGenerate(@RequestBody OTPRequestDto otpRequestDto) {
        return otpService.resetPasswordOtpGenerate(otpRequestDto);
    }

    @PostMapping("/assist/reset-password")
    public void resetPassword(@RequestBody AssistResetPasswordRequestDto passwordRequestDto) {
        otpService.resetPassword(passwordRequestDto);
    }

    @PostMapping("/assist/login")
    public AssistLoginResponseDto login(@RequestBody AssistLoginRequestDto assistLoginRequestDto) {
        return otpService.login(assistLoginRequestDto);
    }

    @GetMapping("/validate")
    public String validate() {
        return VALID_TOKEN;
    }

    @PostMapping("/token/anonymous/request")
    public AnonymousTokenResponseDto generateAnonymousToken(@RequestBody OTPVerifyRequestDto otpVerifyDto) {
        return otpService.generateAnonymousToken(otpVerifyDto);
    }
}
