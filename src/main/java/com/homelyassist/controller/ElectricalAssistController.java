package com.homelyassist.controller;

import com.homelyassist.model.db.ElectricalAssist;
import com.homelyassist.model.rest.request.AssistDetailRequestDTO;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import com.homelyassist.model.rest.response.AssistDetailResponseDto;
import com.homelyassist.model.rest.response.AssistRegistrationResponseDto;
import com.homelyassist.model.rest.response.SearchAssistResponseDto;
import com.homelyassist.service.assist.ElectricalAssistService;
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
@RequestMapping("/api/assist/electrical")
public class ElectricalAssistController {

    private final ElectricalAssistService electricalAssistService;

    @Autowired
    public ElectricalAssistController(ElectricalAssistService electricalAssistService) {
        this.electricalAssistService = electricalAssistService;
    }

    @PostMapping("/register")
    public AssistRegistrationResponseDto register(@RequestBody ElectricalAssist electricalAssist) {
        return electricalAssistService.register(electricalAssist);
    }

    @PostMapping("/{uuid}/image/upload")
    public ResponseEntity<String> uploadImage(@PathVariable("uuid") String id, @RequestParam("file") MultipartFile file) {
        return electricalAssistService.uploadImage(id, file);
    }

    @GetMapping("/{uuid}")
    public ElectricalAssist getDetails(@PathVariable("uuid") String id) {
        return electricalAssistService.fetchById(id);
    }

    @PostMapping("/{uuid}/availability")
    public ElectricalAssist updateAvailability(@PathVariable("uuid") String id, @RequestBody AvailabilityRequestDto availabilityRequestDto) {
        return electricalAssistService.updateAvailability(id, availabilityRequestDto);
    }

    @PostMapping("/search")
    public SearchAssistResponseDto<ElectricalAssist> searchAssist(@RequestBody SearchAssistRequestDto<ElectricalAssist> searchAssistRequestDto) {
        return electricalAssistService.searchAssist(searchAssistRequestDto);
    }

    @PostMapping("/detail")
    public AssistDetailResponseDto getAssistDetails(@RequestBody AssistDetailRequestDTO assistDetailRequestDTO) {
        return electricalAssistService.getAssistDetails(assistDetailRequestDTO);
    }
}
