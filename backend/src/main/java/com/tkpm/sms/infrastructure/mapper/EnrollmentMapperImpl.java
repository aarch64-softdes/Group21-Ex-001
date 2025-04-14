package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.mapper.EnrollmentMapper;
import com.tkpm.sms.application.mapper.StudentMapper;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.domain.model.Enrollment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        StudentMapper.class,
        CourseMapper.class
})
public interface EnrollmentMapperImpl extends EnrollmentMapper {
    @Override
    Enrollment toEnrollment(EnrollmentCreateRequestDto enrollCreateRequestDto);

    @Override
    Enrollment toEnrollment(EnrollmentDeleteRequestDto enrollDeleteRequestDto);

    @Override
    EnrollmentDto toEnrollmentDto(Enrollment enrollment);
}
