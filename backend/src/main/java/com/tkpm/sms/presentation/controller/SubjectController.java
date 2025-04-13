package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.subject.SubjectCreateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectUpdateRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.subject.PrerequisiteSubjectDto;
import com.tkpm.sms.application.dto.response.subject.SubjectDto;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.application.service.interfaces.SubjectService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.utils.ListUtils;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubjectController {
    SubjectService subjectService;
    SubjectMapper subjectMapper;

    @GetMapping({"/", ""})
    public ResponseEntity<ApplicationResponseDto<PageResponse<SubjectDto>>> getSubjects(
         @ModelAttribute BaseCollectionRequest request
    ) {
        PageResponse<Subject> pageResponse = subjectService.findAll(request);
        List<SubjectDto> subjectDtoList = ListUtils.transform(pageResponse.getData(), subjectMapper::toSubjectDto);
        PageResponse<SubjectDto> listResponse = PageResponse.of(pageResponse, subjectDtoList);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationResponseDto<SubjectDto>> getSubjectById(
            @PathVariable Integer id
    ) {
        Subject subject = subjectService.getSubjectById(id);

        List<PrerequisiteSubjectDto> prerequisitesSubjects = subject.getPrerequisites().stream()
                .map(prerequisiteSubject -> new PrerequisiteSubjectDto(
                        prerequisiteSubject.getId(),
                        prerequisiteSubject.getName(),
                        prerequisiteSubject.getCode(),
                        prerequisiteSubject.getDescription()))
                .toList();

        SubjectDto subjectDto = subjectMapper.toSubjectDto(subject);
        subjectDto.setPrerequisitesSubjects(prerequisitesSubjects);
        return ResponseEntity.ok(ApplicationResponseDto.success(subjectDto));
    }

    @PostMapping({"/", ""})
    public ResponseEntity<ApplicationResponseDto<SubjectDto>> createSubject(
            @Valid @RequestBody SubjectCreateRequestDto subjectRequestDto,
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
            @Valid @RequestBody SubjectUpdateRequestDto updateRequestDto
    ) {
        Subject updatedSubject = subjectService.updateSubject(id, updateRequestDto);
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

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<ApplicationResponseDto<Void>> deactivateSubject(
            @PathVariable Integer id
    ) {
        subjectService.deactivateSubject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<ApplicationResponseDto<Void>> activateSubject(
            @PathVariable Integer id
    ) {
        subjectService.deactivateSubject(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
