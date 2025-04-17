package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentCreatedDto;
import com.tkpm.sms.application.dto.response.enrollment.EnrollmentListDto;
import com.tkpm.sms.application.dto.response.enrollment.HistoryDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.mapper.EnrollmentMapper;
import com.tkpm.sms.application.mapper.StudentMapper;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.History;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        StudentMapper.class,
        CourseMapper.class,
})
public interface EnrollmentMapperImpl extends EnrollmentMapper {
    @Override
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "course", ignore = true)
    Enrollment toEnrollment(EnrollmentCreateRequestDto enrollCreateRequestDto);

    @Override
    EnrollmentListDto toEnrollmentListDto(Enrollment enrollment);

    @Override
    @Mapping(target = "transcript.subjectCode", source = "course.subject.code")
    @Mapping(target = "transcript.subjectName", source = "course.subject.name")
    EnrollmentCreatedDto toEnrollmentCreatedDto(Enrollment enrollment);

    @Override
    HistoryDto toHistoryDto(History history);
}
