package com.tkpm.sms.presentation.controller;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentCreatedDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentListDto;
import com.tkpm.sms.application.dto.response.enrollment.HistoryDto;
import com.tkpm.sms.application.mapper.EnrollmentMapper;
import com.tkpm.sms.application.service.interfaces.EnrollmentService;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.History;
import com.tkpm.sms.domain.utils.ListUtils;
import lombok.RequiredArgsConstructor;
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

    @GetMapping("/{studentId}/get-all-enrollments")
    public ResponseEntity<ApplicationResponseDto<PageResponse<EnrollmentListDto>>> getAllEnrollmentsOfStudent(
            @PathVariable String studentId,
            @ModelAttribute BaseCollectionRequest baseCollectionRequest
    ) {
        PageResponse<Enrollment> pageResponse = enrollmentService.findAllEnrollmentsOfStudent(studentId, baseCollectionRequest);
        List<EnrollmentListDto> enrollmentDtos = ListUtils.transform(pageResponse.getData(), enrollmentMapper::toEnrollmentListDto);
        PageResponse<EnrollmentListDto> listResponse = PageResponse.of(pageResponse, enrollmentDtos);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @GetMapping("/{studentId}/get-enrollment-history")
    public ResponseEntity<ApplicationResponseDto<PageResponse<HistoryDto>>> getEnrollmentHistoryOfStudent(
            @PathVariable String studentId,
            @ModelAttribute BaseCollectionRequest baseCollectionRequest
    ) {
        PageResponse<History> pageResponse = enrollmentService.findEnrollmentHistoryOfStudent(studentId, baseCollectionRequest);
        List<HistoryDto> historyDtos = ListUtils.transform(pageResponse.getData(), enrollmentMapper::toHistoryDto);
        PageResponse<HistoryDto> listResponse = PageResponse.of(pageResponse, historyDtos);
        return ResponseEntity.ok(ApplicationResponseDto.success(listResponse));
    }

    @PostMapping("/enroll")
    public ResponseEntity<ApplicationResponseDto<EnrollmentCreatedDto>> enrollStudent(
            @RequestBody EnrollmentCreateRequestDto enrollmentCreateRequestDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        Enrollment enrollment = enrollmentService.createEnrollment(enrollmentCreateRequestDto);
        EnrollmentCreatedDto enrollmentDto = enrollmentMapper.toEnrollmentCreatedDto(enrollment);
        var locationOfNewUser = uriComponentsBuilder.path("api/enrollments/{id}").buildAndExpand(enrollment.getId()).toUri();

        return ResponseEntity.created(locationOfNewUser).body(ApplicationResponseDto.success(enrollmentDto));
    }

    @DeleteMapping("/unenroll")
    public ResponseEntity<?> unenrollStudent(@RequestBody EnrollmentDeleteRequestDto enrollmentDeleteRequestDto) {
        enrollmentService.deleteEnrollment(enrollmentDeleteRequestDto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
