package com.homelyassist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class AvailabilityController {
    @GetMapping("/availability")
    public String AvailabilityPage() {
        return "availability";
    }
}
