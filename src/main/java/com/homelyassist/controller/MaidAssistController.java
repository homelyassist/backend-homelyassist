package com.homelyassist.controller;

import com.homelyassist.model.db.MaidAssist;
import com.homelyassist.model.enums.MaidAssistType;
import com.homelyassist.model.rest.request.AssistDetailRequestDTO;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import com.homelyassist.model.rest.response.AssistDetailResponseDto;
import com.homelyassist.model.rest.response.AssistRegistrationResponseDto;
import com.homelyassist.model.rest.response.SearchAssistResponseDto;
import com.homelyassist.service.assist.MaidAssistService;
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
@RequestMapping("/api/assist/maid")
public class MaidAssistController {

    private final MaidAssistService maidAssistService;

    @Autowired
    public MaidAssistController(MaidAssistService maidAssistService) {
        this.maidAssistService = maidAssistService;
    }


    @PostMapping("/register")
    public AssistRegistrationResponseDto register(@RequestBody MaidAssist maidAssist) {
        return maidAssistService.register(maidAssist);
    }

    @PostMapping("/{uuid}/image/upload")
    public ResponseEntity<String> uploadImage(@PathVariable("uuid") String id, @RequestParam("file") MultipartFile file) {
        return maidAssistService.uploadImage(id, file);
    }

    @GetMapping("/{uuid}")
    public MaidAssist getDetails(@PathVariable("uuid") String id) {
        return maidAssistService.fetchById(id);
    }

    @PostMapping("/{uuid}/availability")
    public MaidAssist updateAvailability(@PathVariable("uuid") String id, @RequestBody AvailabilityRequestDto availabilityRequestDto) {
        return maidAssistService.updateAvailability(id, availabilityRequestDto);
    }

    @PostMapping("/search")
    public SearchAssistResponseDto<MaidAssist> searchAssist(@RequestBody SearchAssistRequestDto<MaidAssistType> searchAssistRequestDto) {
        return maidAssistService.searchAssist(searchAssistRequestDto);
    }

    @PostMapping("/detail")
    public AssistDetailResponseDto getAssistDetails(@RequestBody AssistDetailRequestDTO assistDetailRequestDTO) {
        return maidAssistService.getAssistDetails(assistDetailRequestDTO);
    }
}
