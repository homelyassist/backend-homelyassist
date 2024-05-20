package com.homelyassist.controller;

import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.enums.AgriculturalAssistType;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import com.homelyassist.model.rest.response.AssistRegistrationResponseDto;
import com.homelyassist.model.rest.response.SearchAssistResponseDto;
import com.homelyassist.service.assist.AgriculturalAssistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/assist/agriculture")
public class AgriculturalAssistController {

    private final AgriculturalAssistService agriculturalAssistService;

    @Autowired
    public AgriculturalAssistController(AgriculturalAssistService agriculturalAssistService) {
        this.agriculturalAssistService = agriculturalAssistService;
    }


    @PostMapping("/register")
    public AssistRegistrationResponseDto register(@RequestBody AgriculturalAssist agriculturalAssist) {
        return agriculturalAssistService.register(agriculturalAssist);
    }

    @PostMapping("/{uuid}/image/upload")
    public ResponseEntity<String> uploadImage(@PathVariable("uuid") String id, @RequestParam("file") MultipartFile file) {
        return agriculturalAssistService.uploadImage(id, file);
    }

    @GetMapping("/{uuid}")
    public AgriculturalAssist getDetails(@PathVariable("uuid") String id) {
        return agriculturalAssistService.fetchById(id);
    }

    @PostMapping("/{uuid}/availability")
    public AgriculturalAssist updateAvailability(@PathVariable("uuid") String id, @RequestBody AvailabilityRequestDto availabilityRequestDto) {
        return agriculturalAssistService.updateAvailability(id, availabilityRequestDto);
    }

    @PostMapping("/search")
    public SearchAssistResponseDto<AgriculturalAssist> searchAssist(@RequestBody SearchAssistRequestDto<AgriculturalAssistType> searchAssistRequestDto) {
        return agriculturalAssistService.searchAssist(searchAssistRequestDto);
    }
}
