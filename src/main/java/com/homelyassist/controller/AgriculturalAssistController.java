package com.homelyassist.controller;

import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.response.MemberRegistrationResponseDto;
import com.homelyassist.service.assist.AgriculturalAssistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assist/agriculture")
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

    @GetMapping("/{uuid}")
    public AgriculturalAssist getDetails(@PathVariable("uuid") String id) {
        return agriculturalAssistService.fetchById(id);
    }

    @PostMapping("/{uuid}/availability")
    public AgriculturalAssist updateAvailability(@PathVariable("uuid") String id, @RequestBody AvailabilityRequestDto availabilityRequestDto) {
        return agriculturalAssistService.updateAvailability(id, availabilityRequestDto);
    }
}
