package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST;
import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_AVAILABILITY;
import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_FORGOT_PASSWORD;
import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_LOGIN;
import static com.homelyassist.utils.AppConstant.HTMLPage.ASSIST_REGISTER;
import static com.homelyassist.utils.AppConstant.HTMLPage.SEARCH_AGRICULTURE_ASSIST;
import static com.homelyassist.utils.AppConstant.HTMLPage.SEARCH_ASSIST;
import static com.homelyassist.utils.AppConstant.HTMLPage.SEARCH_CONSTRUCTION_ASSIST;
import static com.homelyassist.utils.AppConstant.HTMLPage.SEARCH_ELECTRICAL_ASSIST;
import static com.homelyassist.utils.AppConstant.HTMLPage.SEARCH_MAID_ASSIST;

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

    @GetMapping("/forgot-password")
    public String forgotPassword() {
        return ASSIST_FORGOT_PASSWORD;
    }

    @GetMapping("/availability")
    public String availability() {
        return ASSIST_AVAILABILITY;
    }

    @GetMapping("/search")
    public String search() {
        return SEARCH_ASSIST;
    }

    @GetMapping("/agriculture/search")
    public String searchAgricultureAssist() {
        return SEARCH_AGRICULTURE_ASSIST;
    }

    @GetMapping("/construction/search")
    public String searchConstructionAssist() {
        return SEARCH_CONSTRUCTION_ASSIST;
    }

    @GetMapping("/electrical/search")
    public String searchElectricalAssist() {
        return SEARCH_ELECTRICAL_ASSIST;
    }

    @GetMapping("/maid/search")
    public String searchMaidAssist() {
        return SEARCH_MAID_ASSIST;
    }

    @GetMapping
    public String assist() {
        return ASSIST;
    }
}
