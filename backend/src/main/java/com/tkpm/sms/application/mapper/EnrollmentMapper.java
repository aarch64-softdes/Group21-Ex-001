package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentDto;
import com.tkpm.sms.domain.model.Enrollment;

public interface EnrollmentMapper {
    Enrollment toEnrollment(EnrollmentCreateRequestDto enrollCreateRequestDto);

    Enrollment toEnrollment(EnrollmentDeleteRequestDto enrollDeleteRequestDto);

    EnrollmentDto toEnrollmentDto(Enrollment enrollment);
}
