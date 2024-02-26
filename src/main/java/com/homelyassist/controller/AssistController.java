package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_AVAILABILITY;
import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_REGISTER;

@Controller
@RequestMapping("/assist")
public class AssistController {

    @GetMapping("/register")
    public String registration(){
        return ASSIST_REGISTER;
    }

    @GetMapping("/availability")
    public String AvailabilityPage() {
        return ASSIST_AVAILABILITY;
    }
}
