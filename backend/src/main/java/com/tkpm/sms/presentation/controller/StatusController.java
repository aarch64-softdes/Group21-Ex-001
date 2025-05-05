package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.application.dto.request.status.StatusVerificationDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.status.AllowedTransitionDto;
import com.tkpm.sms.application.dto.response.status.StatusDto;
import com.tkpm.sms.application.mapper.StatusMapper;
import com.tkpm.sms.application.service.interfaces.StatusService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.utils.ListUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusController {
    StatusService statusService;
    StatusMapper statusMapper;

    @GetMapping
    public ResponseEntity<ApplicationResponseDto<PageResponse<StatusDto>>> getAllStatuses(
            @ModelAttribute BaseCollectionRequest search) {
        PageResponse<Status> pageResponse = statusService.getAllStatuses(search);
        List<StatusDto> statusDtos = ListUtils.transform(pageResponse.getData(),
                statusMapper::toStatusDto);

        PageResponse<StatusDto> listResponse = PageResponse.of(pageResponse, statusDtos);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StatusDto>> getStatus(@PathVariable Integer id) {
        var languageCode = LocaleContextHolder.getLocale().getLanguage();
        Status status = statusService.getStatusById(id);

        List<AllowedTransitionDto> allowedTransitions = status.getValidTransitionIds().stream()
                .map(toId -> {
                    Status toStatus = statusService.getStatusById(toId);
                    return new AllowedTransitionDto(toId, toStatus.getNameByLanguage(languageCode));
                }).collect(Collectors.toList());

        StatusDto statusDto = statusMapper.toStatusDto(status);
        statusDto.setAllowedTransitions(allowedTransitions);

        return ResponseEntity.ok(ApplicationResponseDto.success(statusDto));
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<StatusDto>> createStatus(
            @Valid @RequestBody StatusRequestDto statusRequestDto,
            UriComponentsBuilder uriComponentsBuilder) {
        var languageCode = LocaleContextHolder.getLocale().getLanguage();

        Status status = statusService.createStatus(statusRequestDto);
        StatusDto statusDto = statusMapper.toStatusDto(status);

        return ResponseEntity
                .created(uriComponentsBuilder.path("/api/statuses/{id}")
                        .buildAndExpand(status.getId()).toUri())
                .body(ApplicationResponseDto.success(statusDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StatusDto>> updateStatus(@PathVariable Integer id,
            @Valid @RequestBody StatusRequestDto statusRequestDto) {
        Status status = statusService.updateStatus(id, statusRequestDto);
        StatusDto statusDto = statusMapper.toStatusDto(status);

        return ResponseEntity.ok(ApplicationResponseDto.success(statusDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Void>> deleteStatus(@PathVariable Integer id) {
        statusService.deleteStatus(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/verify-transition")
    public ResponseEntity<ApplicationResponseDto<Boolean>> checkTransitionAllowed(
            @RequestBody StatusVerificationDto statusVerificationDto) {
        boolean allowed = statusService.isTransitionAllowed(statusVerificationDto.getFromId(),
                statusVerificationDto.getToId());
        return ResponseEntity.ok(ApplicationResponseDto.success(allowed));
    }
}