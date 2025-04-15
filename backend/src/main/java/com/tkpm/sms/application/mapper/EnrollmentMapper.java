package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentCreatedDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentListDto;
import com.tkpm.sms.application.dto.response.enrollment.HistoryDto;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.History;

public interface EnrollmentMapper {
    Enrollment toEnrollment(EnrollmentCreateRequestDto enrollCreateRequestDto);

    EnrollmentListDto toEnrollmentListDto(Enrollment enrollment);

    EnrollmentCreatedDto toEnrollmentCreatedDto(Enrollment enrollment);

    HistoryDto toHistoryDto(History history);
}
