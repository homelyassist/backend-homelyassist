package com.homelyassist.service.assist;


import com.homelyassist.exception.MemberAlreadyExistException;
import com.homelyassist.model.db.ElectricalAssist;
import com.homelyassist.model.db.UserMapping;
import com.homelyassist.model.enums.AssistRegistrationStatus;
import com.homelyassist.model.enums.AssistType;
import com.homelyassist.model.rest.request.AssistDetailRequestDTO;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import com.homelyassist.model.rest.response.AssistDetailResponseDto;
import com.homelyassist.model.rest.response.AssistRegistrationResponseDto;
import com.homelyassist.model.rest.response.SearchAssistResponseDto;
import com.homelyassist.query.ElectricalAssistSpecifications;
import com.homelyassist.repository.db.ElectricalAssistRepository;
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
public class ElectricalAssistService {

    private final ElectricalAssistRepository electricalAssistRepository;
    private final UserMappingRepository userMappingRepository;

    @Autowired
    public ElectricalAssistService(ElectricalAssistRepository electricalAssistRepository, UserMappingRepository userMappingRepository) {
        this.electricalAssistRepository = electricalAssistRepository;
        this.userMappingRepository = userMappingRepository;
    }

    public AssistRegistrationResponseDto register(ElectricalAssist electricalAssist) {
        AssistRegistrationResponseDto response = new AssistRegistrationResponseDto();
        try {
            validate(electricalAssist);
            electricalAssist.setActive(Boolean.TRUE);
            electricalAssist.setCreated(LocalDateTime.now());
            electricalAssist.setId(UUID.randomUUID().toString());
            electricalAssist.setPassword(BCryptUtils.encodePassword(electricalAssist.getPassword()));
            electricalAssistRepository.save(electricalAssist);
            UserMapping userMapping = UserMapping.builder()
                    .id(electricalAssist.getId())
                    .phoneNumber(electricalAssist.getPhoneNumber())
                    .password(electricalAssist.getPassword())
                    .assistType(AssistType.ELECTRICAL)
                    .build();
            userMappingRepository.save(userMapping);
            response.setUuid(electricalAssist.getId());
            response.setPhoneNumber(electricalAssist.getPhoneNumber());
            response.setAssistRegistrationStatus(AssistRegistrationStatus.SUCCESSFUL);
        } catch (Exception e) {
            log.error("error while registration agriculture member", e);
            response.setPhoneNumber(electricalAssist.getPhoneNumber());
            response.setAssistRegistrationStatus(AssistRegistrationStatus.ERROR);
            response.setErrorMessage(ErrorUtils.getErrorMessage(e));
        }

        return response;
    }

    public ElectricalAssist fetchById(String id) {
        Objects.requireNonNull(id, "id can't be null");

        return electricalAssistRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Electrical assist with id: {} is not present", id);
                    return new IllegalArgumentException(String.format("Electrical assist with id: %s is not present", id));
                });
    }

    public ResponseEntity<String> uploadImage(String id, MultipartFile file) {
        try {
            Optional<ElectricalAssist> optionalAssist = electricalAssistRepository.findById(id);
            if (optionalAssist.isPresent()) {
                ElectricalAssist electricalAssist = optionalAssist.get();
                electricalAssist.setImage(file.getBytes());
                electricalAssistRepository.save(electricalAssist);
                return ResponseEntity.ok("Image uploaded successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Electrical Assist not found with id: " + id);
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

    public ElectricalAssist updateAvailability(String uuid, AvailabilityRequestDto availabilityRequestDto) {
        ElectricalAssist electricalAssist = fetchById(uuid);
        electricalAssist.setActive(availabilityRequestDto.isAvailability());
        return electricalAssistRepository.save(electricalAssist);
    }

    public SearchAssistResponseDto<ElectricalAssist> searchAssist(SearchAssistRequestDto<ElectricalAssist> searchAssistRequestDto) {
        List<ElectricalAssist> assist = electricalAssistRepository.findAll(ElectricalAssistSpecifications.findBySearchParams(searchAssistRequestDto));
        return new SearchAssistResponseDto<>(assist);
    }

    public AssistDetailResponseDto getAssistDetails(AssistDetailRequestDTO assistDetailRequestDTO) {
        String assistId = assistDetailRequestDTO.getAssistId();
        ElectricalAssist electricalAssist = electricalAssistRepository.findById(assistId).orElseThrow(() -> new RuntimeException(String.format("Assist with id: %s not present", assistId)));
        AssistDetailResponseDto assistDetailResponseDto = new AssistDetailResponseDto();
        assistDetailResponseDto.setName(electricalAssist.getName());
        assistDetailResponseDto.setPhoneNumber(electricalAssist.getPhoneNumber());
        return assistDetailResponseDto;
    }


    private void validate(ElectricalAssist electricalAssist) {
        log.info("start validation for member registration");
        MemberHelper.validateDefaultProperties(electricalAssist);

        if (electricalAssistRepository.existsByPhoneNumber(electricalAssist.getPhoneNumber())) {
            throw new MemberAlreadyExistException(electricalAssist.getPhoneNumber());
        }
        // TODO: specific validation
    }
}
