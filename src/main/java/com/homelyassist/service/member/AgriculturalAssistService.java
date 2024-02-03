package com.homelyassist.service.member;

import com.homelyassist.exception.MemberAlreadyExistException;
import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.enums.MemberRegistrationStatus;
import com.homelyassist.model.rest.response.MemberRegistrationResponseDto;
import com.homelyassist.repository.db.AgriculturalAssistRepository;
import com.homelyassist.utils.ErrorUtils;
import com.homelyassist.utils.MemberHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class AgriculturalAssistService {

    private final AgriculturalAssistRepository agriculturalAssistRepository;


    public AgriculturalAssistService(AgriculturalAssistRepository agriculturalAssistRepository) {
        this.agriculturalAssistRepository = agriculturalAssistRepository;
    }

    public MemberRegistrationResponseDto register(AgriculturalAssist agriculturalAssist) {
        MemberRegistrationResponseDto response = new MemberRegistrationResponseDto();
        try {
            validate(agriculturalAssist);
            agriculturalAssist.setActive(Boolean.TRUE);
            agriculturalAssist.setCreated(LocalDateTime.now());
            agriculturalAssistRepository.save(agriculturalAssist);
            response.setPhoneNumber(agriculturalAssist.getPhoneNumber());
            response.setMemberRegistrationStatus(MemberRegistrationStatus.SUCCESSFUL);
        } catch (Exception e) {
            log.error("error while registration agriculture member", e);
            response.setPhoneNumber(agriculturalAssist.getPhoneNumber());
            response.setMemberRegistrationStatus(MemberRegistrationStatus.ERROR);
            response.setErrorMessage(ErrorUtils.getErrorMessage(e));
        }

        return response;
    }

    public void validate(AgriculturalAssist agriculturalAssist) {
        log.info("start validation for member registration");
        MemberHelper.validateDefaultProperties(agriculturalAssist);

        if (agriculturalAssistRepository.existsByPhoneNumber(agriculturalAssist.getPhoneNumber())) {
            throw new MemberAlreadyExistException(agriculturalAssist.getPhoneNumber());
        }

        // TODO: specific validation
    }
}
