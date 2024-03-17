package com.homelyassist.service;

import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.rest.request.AssistDetailRequestDTO;
import com.homelyassist.model.rest.response.AssistDetailResponseDto;
import com.homelyassist.repository.db.AgriculturalAssistRepository;
import org.springframework.stereotype.Service;

@Service
public class MemberService {

    private final AgriculturalAssistRepository agriculturalAssistRepository;

    public MemberService(AgriculturalAssistRepository agriculturalAssistRepository) {
        this.agriculturalAssistRepository = agriculturalAssistRepository;
    }


    public AssistDetailResponseDto getAssistDetails(AssistDetailRequestDTO assistDetailRequestDTO) {
        String assistId = assistDetailRequestDTO.getAssistId();
        AgriculturalAssist agriculturalAssist = agriculturalAssistRepository.findById(assistId).orElseThrow(() -> new RuntimeException(String.format("Assist with id: %s not present", assistId)));
        AssistDetailResponseDto assistDetailResponseDto = new AssistDetailResponseDto();
        assistDetailResponseDto.setName(agriculturalAssist.getName());
        assistDetailResponseDto.setPhoneNumber(agriculturalAssist.getPhoneNumber());
        return assistDetailResponseDto;
    }
}
