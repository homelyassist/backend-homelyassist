package com.homelyassist.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

    /**
     *
     * For ADMIN, we'll provide fixed username & password
     */

    @GetMapping("/test")
    public String testJWT() {
        return "Working";
    }
}
