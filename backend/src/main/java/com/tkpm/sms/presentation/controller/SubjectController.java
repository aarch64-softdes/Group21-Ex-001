package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.subject.SubjectRequestDto;
import com.tkpm.sms.application.dto.response.subject.PrerequisiteSubjectDto;
import com.tkpm.sms.application.dto.response.subject.SubjectDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.common.ListResponse;
import com.tkpm.sms.application.dto.response.common.PageDto;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.application.service.interfaces.SubjectService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Subject;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectController {
    SubjectService subjectService;
    SubjectMapper subjectMapper;

    @GetMapping({"/", ""})
    public ResponseEntity<ApplicationResponseDto<ListResponse<SubjectDto>>> getSubjects(
         @ModelAttribute BaseCollectionRequest request
    ) {
        PageResponse<Subject> pageResponse = subjectService.findAll(request);

        // Map domain entities to DTOs
        List<SubjectDto> subjectDtoList = pageResponse.getContent().stream()
             .map(subjectMapper::toSubjectDto)
             .toList();

        // Create page info
        var pageDto = PageDto.builder()
             .totalElements(pageResponse.getTotalElements())
             .pageSize(pageResponse.getPageSize())
             .pageNumber(pageResponse.getPageNumber())
             .totalPages(pageResponse.getTotalPages())
             .build();

        // Create response
        var listResponse = ListResponse.<SubjectDto>builder()
             .page(pageDto)
             .data(subjectDtoList)
             .build();

        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<SubjectDto>> getSubjectById(
            @PathVariable Integer id
    ) {
        Subject subject = subjectService.getSubjectById(id);

        List<PrerequisiteSubjectDto> prerequisitesSubjects = subject.getPrerequisitesId().stream()
                .map(prerequisiteId -> {
                    Subject prerequisiteSubject = subjectService.getSubjectById(prerequisiteId);
                    return new PrerequisiteSubjectDto(
                            prerequisiteId,
                            prerequisiteSubject.getName(),
                            prerequisiteSubject.getCode());
                })
                .toList();

        SubjectDto subjectDto = subjectMapper.toSubjectDto(subject);
        subjectDto.setPrerequisitesSubjects(prerequisitesSubjects);
        return ResponseEntity.ok(ApplicationResponseDto.success(subjectDto));
    }

    @PostMapping({"/", ""})
    public ResponseEntity<ApplicationResponseDto<SubjectDto>> createSubject(
            @RequestBody SubjectRequestDto subjectRequestDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Subject createdSubject = subjectService.createSubject(subjectRequestDto);
        SubjectDto createdSubjectDto = subjectMapper.toSubjectDto(createdSubject);
        return ResponseEntity.created(uriComponentsBuilder.path("/api/subjects/{id}").buildAndExpand(createdSubject.getId()).toUri())
                .body(ApplicationResponseDto.success(createdSubjectDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<SubjectDto>> updateSubject(
            @PathVariable Integer id,
            @RequestBody SubjectRequestDto subjectRequestDto
    ) {
        Subject updatedSubject = subjectService.updateSubject(id, subjectRequestDto);
        SubjectDto updatedSubjectDto = subjectMapper.toSubjectDto(updatedSubject);
        return ResponseEntity.ok(ApplicationResponseDto.success(updatedSubjectDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<Void>> deleteSubject(
            @PathVariable Integer id
    ) {
        subjectService.deleteSubject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
