package com.tkpm.sms.controller;

import com.tkpm.sms.dto.request.status.StatusRequestDto;
import com.tkpm.sms.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.dto.response.StatusDto;
import com.tkpm.sms.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.dto.response.common.ListResponse;
import com.tkpm.sms.dto.response.common.PageDto;
import com.tkpm.sms.mapper.StatusMapper;
import com.tkpm.sms.service.StatusService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

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
        Page<StatusDto> statuses = statusService
                .getAllStatuses(search)
                .map(statusMapper::toStatusDto);

        var pageDto = PageDto.builder()
                .totalElements(statuses.getTotalElements())
                .pageSize(statuses.getSize())
                .pageNumber(statuses.getNumber())
                .totalPages(statuses.getTotalPages())
                .build();

        var listResponse = ListResponse.<StatusDto>builder().
                page(pageDto).
                data(statuses.stream().toList()).
                build();

        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StatusDto>> getStatus(@PathVariable Integer id) {
        var status = statusService.getStatus(id);
//        var validTransitions = status.getValidTransitionIds().stream().map(statusTransition -> statusTransition.getToStatus().getId()).toList();
        var statusDto = new StatusDto(
                status.getId(), 
                status.getName(),
                status.getValidTransitionIds());

        return ResponseEntity.ok(ApplicationResponseDto.success(statusDto));
    }

    @PostMapping
    public ResponseEntity<ApplicationResponseDto<StatusDto>> createStatus(
            @Valid @RequestBody StatusRequestDto status,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var newStatus = statusService.createStatus(status);
        var statusDto = new StatusDto(newStatus.getId(), newStatus.getName(), newStatus.getValidTransitionIds());

        return ResponseEntity.created(uriComponentsBuilder.path("/api/statuses/{id}").buildAndExpand(newStatus.getId()).toUri())
                .body(ApplicationResponseDto.success(statusDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<StatusDto>> updateStatus(
            @PathVariable Integer id,
            @Valid @RequestBody StatusRequestDto status
    ) {
        var updatedStatus = statusService.updateStatus(id, status);
        var statusDto = new StatusDto(updatedStatus.getId(), updatedStatus.getName(), updatedStatus.getValidTransitionIds());

        return ResponseEntity.ok(ApplicationResponseDto.success(statusDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Void>> deleteStatus(@PathVariable Integer id) {
        statusService.deleteStatus(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

//    @GetMapping("/transitions")
//    public ResponseEntity<List<StatusTransitionResponseDto>> getAllTransitions() {
//        return ResponseEntity.ok(statusService.getAllTransitions());
//    }

    // TODO: Implement this 
//    @GetMapping("/verify-transition")
//    public ResponseEntity<Boolean> checkTransitionAllowed(
//            @RequestParam Integer fromStatusId,
//            @RequestParam Integer toStatusId) {
//        boolean allowed = statusService.isTransitionAllowed(fromStatusId, toStatusId);
//        return ResponseEntity.ok(allowed);
//    }
}
