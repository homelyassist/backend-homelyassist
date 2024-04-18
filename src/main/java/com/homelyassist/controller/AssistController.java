package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST;
import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_AVAILABILITY;
import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_LOGIN;
import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_REGISTER;
import static com.homelyassist.utils.AppConstant.HTMLPage.SEARCH_ASSIST;

@Controller
@RequestMapping("/assist")
public class AssistController {

    @GetMapping("/register")
    public String registration(){
        return ASSIST_REGISTER;
    }

    @GetMapping("/login")
    public String login() {
        return ASSIST_LOGIN;
    }

    @GetMapping("/availability")
    public String availability() {
        return ASSIST_AVAILABILITY;
    }

    @GetMapping("/search")
    public String search() {
        return SEARCH_ASSIST;
    }

    @GetMapping
    public String assist() {
        return ASSIST;
    }
}
