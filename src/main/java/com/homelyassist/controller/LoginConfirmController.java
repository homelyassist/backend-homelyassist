package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginConfirmController {
    @GetMapping("/loginconfirm")
    public String LoginPage(){
        return "loginconfirm";
    }
}
