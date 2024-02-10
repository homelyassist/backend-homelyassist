package com.homelyassist.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    /**
     *
     * For ADMIN, we'll provide fixed username & password
     */

    @GetMapping("/")
    public String testJWT() {
        return "index";
    }
}
