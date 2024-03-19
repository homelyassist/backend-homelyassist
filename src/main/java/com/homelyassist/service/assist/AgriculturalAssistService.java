package com.homelyassist.service.assist;

import com.homelyassist.exception.MemberAlreadyExistException;
import com.homelyassist.model.db.AgriculturalAssist;
import com.homelyassist.model.db.UserMapping;
import com.homelyassist.model.enums.AgriculturalAssistType;
import com.homelyassist.model.enums.AssistType;
import com.homelyassist.model.enums.AssistRegistrationStatus;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import com.homelyassist.model.rest.response.AssistRegistrationResponseDto;
import com.homelyassist.model.rest.response.SearchAssistResponseDto;
import com.homelyassist.repository.db.AgriculturalAssistRepository;
import com.homelyassist.repository.db.UserMappingRepository;
import com.homelyassist.utils.ErrorUtils;
import com.homelyassist.utils.MemberHelper;
import lombok.extern.slf4j.Slf4j;
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
public class AgriculturalAssistService {

    private final AgriculturalAssistRepository agriculturalAssistRepository;
    private final UserMappingRepository userMappingRepository;


    public AgriculturalAssistService(AgriculturalAssistRepository agriculturalAssistRepository, UserMappingRepository userMappingRepository) {
        this.agriculturalAssistRepository = agriculturalAssistRepository;
        this.userMappingRepository = userMappingRepository;
    }

    public AssistRegistrationResponseDto register(AgriculturalAssist agriculturalAssist) {
        AssistRegistrationResponseDto response = new AssistRegistrationResponseDto();
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
            response.setAssistRegistrationStatus(AssistRegistrationStatus.SUCCESSFUL);
        } catch (Exception e) {
            log.error("error while registration agriculture member", e);
            response.setPhoneNumber(agriculturalAssist.getPhoneNumber());
            response.setAssistRegistrationStatus(AssistRegistrationStatus.ERROR);
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

    public SearchAssistResponseDto searchAssist(SearchAssistRequestDto searchAssistRequestDto) {
        String area = searchAssistRequestDto.getCityArea();
        String pinCode = searchAssistRequestDto.getPinCode();
        List<AgriculturalAssistType> assistTypes = searchAssistRequestDto.getAssistTypes();

        List<AgriculturalAssist> assist = agriculturalAssistRepository.findTop50ByPinCodeAndAgriculturalAssistTypesInAndCityAreaIgnoreCaseStartingWithAndActiveIsTrue(pinCode, assistTypes, area);
        return new SearchAssistResponseDto(assist);
    }

    private void validate(AgriculturalAssist agriculturalAssist) {
        log.info("start validation for member registration");
        MemberHelper.validateDefaultProperties(agriculturalAssist);

        if (agriculturalAssistRepository.existsByPhoneNumber(agriculturalAssist.getPhoneNumber())) {
            throw new MemberAlreadyExistException(agriculturalAssist.getPhoneNumber());
        }

        // TODO: specific validation
    }

    public ResponseEntity<String> uploadImage(String id, MultipartFile file) {
        try {
            Optional<AgriculturalAssist> optionalAssist = agriculturalAssistRepository.findById(id);
            if (optionalAssist.isPresent()) {
                AgriculturalAssist agriculturalAssist = optionalAssist.get();
                agriculturalAssist.setImage(file.getBytes());
                agriculturalAssistRepository.save(agriculturalAssist);
                return ResponseEntity.ok("Image uploaded successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Agricultural Assist not found with id: " + id);
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
}
