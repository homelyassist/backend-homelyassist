package com.homelyassist.controller;

import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.rest.response.MemberRegistrationResponseDto;
import com.homelyassist.service.member.AgriculturalAssistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/member/agriculture")
public class AgriculturalAssistController {

    private final AgriculturalAssistService agriculturalAssistService;

    @Autowired
    public AgriculturalAssistController(AgriculturalAssistService agriculturalAssistService) {
        this.agriculturalAssistService = agriculturalAssistService;
    }


    @PostMapping("/register")
    public MemberRegistrationResponseDto register(@RequestBody AgriculturalAssist agriculturalAssist) {
        return agriculturalAssistService.register(agriculturalAssist);
    }
}
