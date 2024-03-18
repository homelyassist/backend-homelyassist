package com.homelyassist.controller;

import com.homelyassist.model.rest.request.AssistDetailRequestDTO;
import com.homelyassist.model.rest.response.AssistDetailResponseDto;
import com.homelyassist.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/member/assist/detail")
    public AssistDetailResponseDto getAssistDetails(@RequestBody AssistDetailRequestDTO assistDetailRequestDTO) {
        return memberService.getAssistDetails(assistDetailRequestDTO);
    }
}
