package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.application.dto.request.status.StatusVerificationDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.common.ListResponse;
import com.tkpm.sms.application.dto.response.common.PageDto;
import com.tkpm.sms.application.dto.response.status.AllowedTransitionDto;
import com.tkpm.sms.application.dto.response.status.StatusDto;
import com.tkpm.sms.application.mapper.StatusMapper;
import com.tkpm.sms.application.service.interfaces.StatusService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Status;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/statuses")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class StatusController {
    StatusService statusService;
    StatusMapper statusMapper;

    @GetMapping
    public ResponseEntity<ApplicationResponseDto<ListResponse<StatusDto>>> getAllStatuses(
            @ModelAttribute BaseCollectionRequest search
    ) {
        PageResponse<Status> pageResponse = statusService.getAllStatuses(search);

        // Map domain entities to DTOs
        List<StatusDto> statusDtos = pageResponse.getContent().stream()
                .map(statusMapper::toStatusDto)
                .collect(Collectors.toList());

        // Create page info
        var pageDto = PageDto.builder()
                .totalElements(pageResponse.getTotalElements())
                .pageSize(pageResponse.getPageSize())
                .pageNumber(pageResponse.getPageNumber())
                .totalPages(pageResponse.getTotalPages())
                .build();

        // Create response
        var listResponse = ListResponse.<StatusDto>builder()
                .page(pageDto)
                .data(statusDtos)
                .build();

        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StatusDto>> getStatus(@PathVariable Integer id) {
        Status status = statusService.getStatusById(id);

        List<AllowedTransitionDto> allowedTransitions = status.getValidTransitionIds().stream()
                .map(toId -> {
                    Status toStatus = statusService.getStatusById(toId);
                    return new AllowedTransitionDto(toId, toStatus.getName());
                })
                .collect(Collectors.toList());

        StatusDto statusDto = statusMapper.toStatusDto(status);
        statusDto.setAllowedTransitions(allowedTransitions);

        return ResponseEntity.ok(ApplicationResponseDto.success(statusDto));
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<StatusDto>> createStatus(
            @Valid @RequestBody StatusRequestDto statusRequestDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Status status = statusService.createStatus(statusRequestDto);
        StatusDto statusDto = statusMapper.toStatusDto(status);

        return ResponseEntity.created(uriComponentsBuilder.path("/api/statuses/{id}").buildAndExpand(status.getId()).toUri())
                .body(ApplicationResponseDto.success(statusDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StatusDto>> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody StatusRequestDto statusRequestDto
    ) {
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
        boolean allowed = statusService.isTransitionAllowed(
                statusVerificationDto.getFromId(),
                statusVerificationDto.getToId());
        return ResponseEntity.ok(ApplicationResponseDto.success(allowed));
    }
}