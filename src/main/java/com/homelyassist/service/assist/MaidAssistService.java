package com.homelyassist.service.assist;

import com.homelyassist.exception.MemberAlreadyExistException;
import com.homelyassist.model.db.MaidAssist;
import com.homelyassist.model.db.UserMapping;
import com.homelyassist.model.enums.AssistRegistrationStatus;
import com.homelyassist.model.enums.AssistType;
import com.homelyassist.model.enums.MaidAssistType;
import com.homelyassist.model.rest.request.AssistDetailRequestDTO;
import com.homelyassist.model.rest.request.AvailabilityRequestDto;
import com.homelyassist.model.rest.request.SearchAssistRequestDto;
import com.homelyassist.model.rest.response.AssistDetailResponseDto;
import com.homelyassist.model.rest.response.AssistRegistrationResponseDto;
import com.homelyassist.model.rest.response.SearchAssistResponseDto;
import com.homelyassist.query.MaidAssistSpecifications;
import com.homelyassist.repository.db.MaidAssistRepository;
import com.homelyassist.repository.db.UserMappingRepository;
import com.homelyassist.utils.BCryptUtils;
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
public class MaidAssistService {

    private final MaidAssistRepository maidAssistRepository;
    private final UserMappingRepository userMappingRepository;


    public MaidAssistService(MaidAssistRepository maidAssistRepository, UserMappingRepository userMappingRepository) {
        this.maidAssistRepository = maidAssistRepository;
        this.userMappingRepository = userMappingRepository;
    }

    public AssistRegistrationResponseDto register(MaidAssist maidAssist) {
        AssistRegistrationResponseDto response = new AssistRegistrationResponseDto();
        try {
            validate(maidAssist);
            maidAssist.setActive(Boolean.TRUE);
            maidAssist.setCreated(LocalDateTime.now());
            maidAssist.setId(UUID.randomUUID().toString());
            maidAssist.setPassword(BCryptUtils.encodePassword(maidAssist.getPassword()));
            maidAssistRepository.save(maidAssist);
            UserMapping userMapping = UserMapping.builder()
                    .id(maidAssist.getId())
                    .phoneNumber(maidAssist.getPhoneNumber())
                    .password(maidAssist.getPassword())
                    .assistType(AssistType.AGRICULTURE)
                    .build();
            userMappingRepository.save(userMapping);
            response.setUuid(maidAssist.getId());
            response.setPhoneNumber(maidAssist.getPhoneNumber());
            response.setAssistRegistrationStatus(AssistRegistrationStatus.SUCCESSFUL);
        } catch (Exception e) {
            log.error("error while registration maid member", e);
            response.setPhoneNumber(maidAssist.getPhoneNumber());
            response.setAssistRegistrationStatus(AssistRegistrationStatus.ERROR);
            response.setErrorMessage(ErrorUtils.getErrorMessage(e));
        }

        return response;
    }

    public MaidAssist fetchById(String id) {
        Objects.requireNonNull(id, "id can't be null");

        return maidAssistRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Maid assist with id: {} is not present", id);
                    return new IllegalArgumentException(String.format("Maid assist with id: %s is not present", id));
                });
    }

    public MaidAssist updateAvailability(String uuid, AvailabilityRequestDto availabilityRequestDto) {
        MaidAssist maidAssist = fetchById(uuid);
        maidAssist.setActive(availabilityRequestDto.isAvailability());
        return maidAssistRepository.save(maidAssist);
    }

    public SearchAssistResponseDto<MaidAssist> searchAssist(SearchAssistRequestDto<MaidAssistType> searchAssistRequestDto) {
        List<MaidAssist> assist = maidAssistRepository.findAll(MaidAssistSpecifications.findBySearchParams(searchAssistRequestDto));
        return new SearchAssistResponseDto<>(assist);
    }

    public AssistDetailResponseDto getAssistDetails(AssistDetailRequestDTO assistDetailRequestDTO) {
        String assistId = assistDetailRequestDTO.getAssistId();
        MaidAssist maidAssist = maidAssistRepository.findById(assistId).orElseThrow(() -> new RuntimeException(String.format("Assist with id: %s not present", assistId)));
        AssistDetailResponseDto assistDetailResponseDto = new AssistDetailResponseDto();
        assistDetailResponseDto.setName(maidAssist.getName());
        assistDetailResponseDto.setPhoneNumber(maidAssist.getPhoneNumber());
        return assistDetailResponseDto;
    }

    private void validate(MaidAssist maidAssist) {
        log.info("start validation for member registration");
        MemberHelper.validateDefaultProperties(maidAssist);

        if (maidAssistRepository.existsByPhoneNumber(maidAssist.getPhoneNumber())) {
            throw new MemberAlreadyExistException(maidAssist.getPhoneNumber());
        }

        // TODO: specific validation
    }

    public ResponseEntity<String> uploadImage(String id, MultipartFile file) {
        try {
            Optional<MaidAssist> optionalAssist = maidAssistRepository.findById(id);
            if (optionalAssist.isPresent()) {
                MaidAssist maidAssist = optionalAssist.get();
                maidAssist.setImage(file.getBytes());
                maidAssistRepository.save(maidAssist);
                return ResponseEntity.ok("Image uploaded successfully");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Maid Assist not found with id: " + id);
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
