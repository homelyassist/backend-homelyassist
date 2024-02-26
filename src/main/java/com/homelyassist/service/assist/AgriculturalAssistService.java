package com.homelyassist.service.assist;

import com.homelyassist.exception.MemberAlreadyExistException;
import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.db.UserMapping;
import com.homelyassist.model.enums.AssistType;
import com.homelyassist.model.enums.MemberRegistrationStatus;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.response.MemberRegistrationResponseDto;
import com.homelyassist.repository.db.AgriculturalAssistRepository;
import com.homelyassist.repository.db.UserMappingRepository;
import com.homelyassist.utils.ErrorUtils;
import com.homelyassist.utils.MemberHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class AgriculturalAssistService {

    private final AgriculturalAssistRepository agriculturalAssistRepository;
    private final UserMappingRepository userMappingRepository;


    public AgriculturalAssistService(AgriculturalAssistRepository agriculturalAssistRepository, UserMappingRepository userMappingRepository) {
        this.agriculturalAssistRepository = agriculturalAssistRepository;
        this.userMappingRepository = userMappingRepository;
    }

    public MemberRegistrationResponseDto register(AgriculturalAssist agriculturalAssist) {
        MemberRegistrationResponseDto response = new MemberRegistrationResponseDto();
        try {
            validate(agriculturalAssist);
            agriculturalAssist.setActive(Boolean.TRUE);
            agriculturalAssist.setCreated(LocalDateTime.now());
            agriculturalAssist.setId(UUID.randomUUID().toString());
            agriculturalAssistRepository.save(agriculturalAssist);
            UserMapping userMapping = UserMapping.builder()
                    .id(agriculturalAssist.getId())
                    .phoneNumber(agriculturalAssist.getPhoneNumber())
                    .assistType(AssistType.AGRICULTURE)
                    .build();
            userMappingRepository.save(userMapping);
            response.setUuid(agriculturalAssist.getId());
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

    public AgriculturalAssist fetchById(String id) {
        Objects.requireNonNull(id, "id can't be null");

        return agriculturalAssistRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Agricultural assist with id: {} is not present", id);
                    return new IllegalArgumentException(String.format("Agriculture assist with id: %s is not present", id));
                });
    }

    public AgriculturalAssist updateAvailability(String uuid, AvailabilityRequestDto availabilityRequestDto) {
        AgriculturalAssist agriculturalAssist = fetchById(uuid);
        agriculturalAssist.setActive(availabilityRequestDto.isAvailability());
        return agriculturalAssistRepository.save(agriculturalAssist);
    }

    private void validate(AgriculturalAssist agriculturalAssist) {
        log.info("start validation for member registration");
        MemberHelper.validateDefaultProperties(agriculturalAssist);

        if (agriculturalAssistRepository.existsByPhoneNumber(agriculturalAssist.getPhoneNumber())) {
            throw new MemberAlreadyExistException(agriculturalAssist.getPhoneNumber());
        }

        // TODO: specific validation
    }
}
