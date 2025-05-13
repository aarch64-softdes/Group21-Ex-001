package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentMinimalDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentHistoryDto;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.valueobject.EnrollmentHistory;

public interface EnrollmentMapper {
    Enrollment toEnrollment(EnrollmentCreateRequestDto enrollCreateRequestDto);

    EnrollmentMinimalDto toEnrollmentListDto(Enrollment enrollment);

    EnrollmentDto toEnrollmentCreatedDto(Enrollment enrollment);

    EnrollmentHistoryDto toHistoryDto(EnrollmentHistory enrollmentHistory);
}
