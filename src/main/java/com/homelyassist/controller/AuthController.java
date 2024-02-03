package com.homelyassist.controller;

import com.homelyassist.model.rest.request.OTPRequestDto;
import com.homelyassist.model.rest.response.OTPResponseDto;
import com.homelyassist.model.rest.request.OTPVerifyRequestDto;
import com.homelyassist.model.rest.response.OTPVerifyResponseDto;
import com.homelyassist.service.otp.OTPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return otpService.generateOTP(otpRequestDto);
    }

    @PostMapping("/otp/verify")
    public OTPVerifyResponseDto verifyOtp(@RequestBody OTPVerifyRequestDto otpVerifyDto) {
        return otpService.validateOTP(otpVerifyDto);
    }
}
