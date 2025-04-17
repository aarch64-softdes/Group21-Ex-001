package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.TranscriptUpdateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.AcademicTranscriptDto;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.History;

public interface EnrollmentService {
    PageResponse<Enrollment> findAllEnrollmentsOfStudent(String studentId, BaseCollectionRequest baseCollectionRequest);

    PageResponse<History> findEnrollmentHistoryOfStudent(String studentId, BaseCollectionRequest baseCollectionRequest);

    Enrollment updateTranscriptOfEnrollment(String studentId, Integer courseId, TranscriptUpdateRequestDto transcriptUpdateRequestDto);

    Enrollment createEnrollment(EnrollmentCreateRequestDto enrollmentCreateRequestDto);

    void deleteEnrollment(EnrollmentDeleteRequestDto enrollmentDeleteRequestDto);

    AcademicTranscriptDto getAcademicTranscriptOfStudent(String studentId);
}
