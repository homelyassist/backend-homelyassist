package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class SearchConfirmController {
    @GetMapping("/searchconfirm")
    public String SearchPage(){
        return "searchconfirm";
    }
}
