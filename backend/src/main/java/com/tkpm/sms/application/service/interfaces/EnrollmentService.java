package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentFileImportDto;
import com.tkpm.sms.application.dto.request.enrollment.TranscriptUpdateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.AcademicTranscriptDto;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.valueobject.EnrollmentHistory;

import java.util.List;

public interface EnrollmentService {
    PageResponse<Enrollment> findAllEnrollmentsOfStudent(String studentId,
            BaseCollectionRequest baseCollectionRequest, String languageCode);

    PageResponse<EnrollmentHistory> findEnrollmentHistoryOfStudent(String studentId,
            BaseCollectionRequest baseCollectionRequest, String languageCode);

    void updateTranscriptOfEnrollment(String studentId, Integer courseId,
            TranscriptUpdateRequestDto transcriptUpdateRequestDto);

    void updateTranscripts(List<EnrollmentFileImportDto> enrollmentFileImportDtos);

    Enrollment createEnrollment(EnrollmentCreateRequestDto enrollmentCreateRequestDto,
            String languageCode);

    void deleteEnrollment(EnrollmentDeleteRequestDto enrollmentDeleteRequestDto);

    AcademicTranscriptDto getAcademicTranscriptOfStudent(String studentId, String languageCode);
}
