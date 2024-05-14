package com.homelyassist.controller;

import com.homelyassist.model.db.ConstructionAssist;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import com.homelyassist.model.rest.response.AssistRegistrationResponseDto;
import com.homelyassist.model.rest.response.SearchAssistResponseDto;
import com.homelyassist.service.assist.ConstructionAssistService;
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
@RequestMapping("/api/assist/construction")
public class ConstructionAssistController {

    private final ConstructionAssistService constructionAssistService;

    @Autowired
    public ConstructionAssistController(ConstructionAssistService constructionAssistService) {
        this.constructionAssistService = constructionAssistService;
    }


    @PostMapping("/register")
    public AssistRegistrationResponseDto register(@RequestBody ConstructionAssist constructionAssist) {
        return null;
    }

    @PostMapping("/{uuid}/image/upload")
    public ResponseEntity<String> uploadImage(@PathVariable("uuid") String id, @RequestParam("file") MultipartFile file) {
        return null;
    }

    @GetMapping("/{uuid}")
    public ConstructionAssist getDetails(@PathVariable("uuid") String id) {
        return null;
    }

    @PostMapping("/{uuid}/availability")
    public ConstructionAssist updateAvailability(@PathVariable("uuid") String id, @RequestBody AvailabilityRequestDto availabilityRequestDto) {
        return null;
    }

    @PostMapping("/search")
    public SearchAssistResponseDto searchAssist(@RequestBody SearchAssistRequestDto searchAssistRequestDto) {
        return null;
    }
}