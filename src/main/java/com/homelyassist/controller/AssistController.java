package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_REGISTER;

@Controller
public class AssistController {

    @GetMapping("/assist/register")
    public String registration(){
        return ASSIST_REGISTER;
    }
}
