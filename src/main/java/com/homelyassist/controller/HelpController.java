package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class HelpController {
    @GetMapping("/help")
    public String HelpPage(){
        return "help";
    }
}
