package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.homelyassist.utils.AppConstant.HTMLPage.HOME_PAGE;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homePage() {
        return HOME_PAGE;
    }
}





