package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class RegistrationConfirmController {
    @GetMapping("/registrationconfirm")
    public String RegistrationPage(){
        return "registrationconfirm";
    }
}
