package com.homelyassist.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class OtpController {
    @GetMapping("/otpconfirm")
    public String RegisterAssistPage(){
        return "otpconfirm";
    }
}
