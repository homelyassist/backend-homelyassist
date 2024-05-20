package com.homelyassist.service.assist;


import com.homelyassist.exception.MemberAlreadyExistException;
import com.homelyassist.model.db.ConstructionAssist;
import com.homelyassist.model.db.UserMapping;
import com.homelyassist.model.enums.AssistRegistrationStatus;
import com.homelyassist.model.enums.AssistType;
import com.homelyassist.model.enums.ConstructionAssistType;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import com.homelyassist.model.rest.response.AssistRegistrationResponseDto;
import com.homelyassist.model.rest.response.SearchAssistResponseDto;
import com.homelyassist.query.ConstructionAssistSpecifications;
import com.homelyassist.repository.db.ConstructionAssistRepository;
import com.homelyassist.repository.db.UserMappingRepository;
import com.homelyassist.utils.BCryptUtils;
import com.homelyassist.utils.ErrorUtils;
import com.homelyassist.utils.MemberHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class ConstructionAssistService {

    private final ConstructionAssistRepository constructionAssistRepository;
    private final UserMappingRepository userMappingRepository;

    @Autowired
    public ConstructionAssistService(ConstructionAssistRepository constructionAssistRepository, UserMappingRepository userMappingRepository) {
        this.constructionAssistRepository = constructionAssistRepository;
        this.userMappingRepository = userMappingRepository;
    }

    public AssistRegistrationResponseDto register(ConstructionAssist constructionAssist) {
        AssistRegistrationResponseDto response = new AssistRegistrationResponseDto();
        try {
            validate(constructionAssist);
            constructionAssist.setActive(Boolean.TRUE);
            constructionAssist.setCreated(LocalDateTime.now());
            constructionAssist.setId(UUID.randomUUID().toString());
            constructionAssist.setPassword(BCryptUtils.encodePassword(constructionAssist.getPassword()));
            constructionAssistRepository.save(constructionAssist);
            UserMapping userMapping = UserMapping.builder()
                    .id(constructionAssist.getId())
                    .phoneNumber(constructionAssist.getPhoneNumber())
                    .password(constructionAssist.getPassword())
                    .assistType(AssistType.CONSTRUCTION)
                    .build();
            userMappingRepository.save(userMapping);
            response.setUuid(constructionAssist.getId());
            response.setPhoneNumber(constructionAssist.getPhoneNumber());
            response.setAssistRegistrationStatus(AssistRegistrationStatus.SUCCESSFUL);
        } catch (Exception e) {
            log.error("error while registration agriculture member", e);
            response.setPhoneNumber(constructionAssist.getPhoneNumber());
            response.setAssistRegistrationStatus(AssistRegistrationStatus.ERROR);
            response.setErrorMessage(ErrorUtils.getErrorMessage(e));
        }

        return response;
    }

    public ConstructionAssist fetchById(String id) {
        Objects.requireNonNull(id, "id can't be null");

        return constructionAssistRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Construction assist with id: {} is not present", id);
                    return new IllegalArgumentException(String.format("Construction assist with id: %s is not present", id));
                });
    }

    public ResponseEntity<String> uploadImage(String id, MultipartFile file) {
        try {
            Optional<ConstructionAssist> optionalAssist = constructionAssistRepository.findById(id);
            if (optionalAssist.isPresent()) {
                ConstructionAssist constructionAssist = optionalAssist.get();
                constructionAssist.setImage(file.getBytes());
                constructionAssistRepository.save(constructionAssist);
                return ResponseEntity.ok("Image uploaded successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Construction Assist not found with id: " + id);
            }
        } catch (IOException e) {
            // Handle IOException
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to read file data");
        } catch (Exception e) {
            // Log any other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    public ConstructionAssist updateAvailability(String uuid, AvailabilityRequestDto availabilityRequestDto) {
        ConstructionAssist constructionAssist = fetchById(uuid);
        constructionAssist.setActive(availabilityRequestDto.isAvailability());
        return constructionAssistRepository.save(constructionAssist);
    }

    public SearchAssistResponseDto<ConstructionAssist> searchAssist(SearchAssistRequestDto<ConstructionAssistType> searchAssistRequestDto) {
        List<ConstructionAssist> assist = constructionAssistRepository.findAll(ConstructionAssistSpecifications.findBySearchParams(searchAssistRequestDto));
        return new SearchAssistResponseDto<>(assist);
    }


    private void validate(ConstructionAssist constructionAssist) {
        log.info("start validation for member registration");
        MemberHelper.validateDefaultProperties(constructionAssist);

        if (constructionAssistRepository.existsByPhoneNumber(constructionAssist.getPhoneNumber())) {
            throw new MemberAlreadyExistException(constructionAssist.getPhoneNumber());
        }
        // TODO: specific validation
    }
}
