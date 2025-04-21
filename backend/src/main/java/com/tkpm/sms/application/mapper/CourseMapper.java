package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.dto.response.course.CourseDto;
import com.tkpm.sms.application.dto.response.course.CourseMinimalDto;
import com.tkpm.sms.domain.model.Course;
import org.mapstruct.MappingTarget;

public interface CourseMapper {
    Course toDomain(CourseCreateRequestDto createRequestDto);

    void toDomain(@MappingTarget Course course, CourseUpdateRequestDto updateRequestDto);

    CourseDto toDto(Course entity);

    CourseMinimalDto toMinimalDto(Course entity);
}
