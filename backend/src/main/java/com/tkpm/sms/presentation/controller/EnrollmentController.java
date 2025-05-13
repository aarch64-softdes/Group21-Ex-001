package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentHistoryDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentMinimalDto;
import com.tkpm.sms.application.mapper.EnrollmentMapper;
import com.tkpm.sms.application.service.interfaces.EnrollmentService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.utils.ListUtils;
import com.tkpm.sms.domain.valueobject.EnrollmentHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/enrollments/")
@RequiredArgsConstructor
public class EnrollmentController {
    private final EnrollmentService enrollmentService;
    private final EnrollmentMapper enrollmentMapper;

    @GetMapping("/{studentId}")
    public ResponseEntity<ApplicationResponseDto<PageResponse<EnrollmentMinimalDto>>> getAllEnrollmentsOfStudent(
            @PathVariable String studentId,
            @ModelAttribute BaseCollectionRequest baseCollectionRequest) {
        PageResponse<Enrollment> pageResponse = enrollmentService.findAllEnrollmentsOfStudent(
                studentId, baseCollectionRequest, LocaleContextHolder.getLocale().getLanguage());
        List<EnrollmentMinimalDto> enrollmentDtos = ListUtils.transform(pageResponse.getData(),
                enrollmentMapper::toEnrollmentListDto);
        PageResponse<EnrollmentMinimalDto> listResponse = PageResponse.of(pageResponse,
                enrollmentDtos);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{studentId}/history")
    public ResponseEntity<ApplicationResponseDto<PageResponse<EnrollmentHistoryDto>>> getEnrollmentHistoryOfStudent(
            @PathVariable String studentId,
            @ModelAttribute BaseCollectionRequest baseCollectionRequest) {
        PageResponse<EnrollmentHistory> pageResponse = enrollmentService
                .findEnrollmentHistoryOfStudent(studentId, baseCollectionRequest,
                        LocaleContextHolder.getLocale().getLanguage());
        List<EnrollmentHistoryDto> enrollmentHistoryDtos = ListUtils
                .transform(pageResponse.getData(), enrollmentMapper::toHistoryDto);
        PageResponse<EnrollmentHistoryDto> listResponse = PageResponse.of(pageResponse,
                enrollmentHistoryDtos);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApplicationResponseDto<EnrollmentDto>> enrollStudent(
            @RequestBody EnrollmentCreateRequestDto enrollmentCreateRequestDto,
            UriComponentsBuilder uriComponentsBuilder) {
        Enrollment enrollment = enrollmentService.createEnrollment(enrollmentCreateRequestDto,
                LocaleContextHolder.getLocale().getLanguage());
        EnrollmentDto enrollmentDto = enrollmentMapper.toEnrollmentCreatedDto(enrollment);
        var locationOfNewUser = uriComponentsBuilder.path("api/enrollments/{id}")
                .buildAndExpand(enrollment.getId()).toUri();

        return ResponseEntity.created(locationOfNewUser)
                .body(ApplicationResponseDto.success(enrollmentDto));
    }

    @DeleteMapping("/unenroll")
    public ResponseEntity<Void> unenrollStudent(
            @RequestBody EnrollmentDeleteRequestDto enrollmentDeleteRequestDto) {
        enrollmentService.deleteEnrollment(enrollmentDeleteRequestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
