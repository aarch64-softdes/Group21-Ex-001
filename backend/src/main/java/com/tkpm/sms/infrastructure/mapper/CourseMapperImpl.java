package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.dto.response.course.CourseDto;
import com.tkpm.sms.application.dto.response.course.CourseMinimalDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.domain.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CourseMapperImpl extends CourseMapper {
    @Mapping(target = "schedule", expression = "java(createRequestDto.getSchedule().toString())")
    Course toDomain(CourseCreateRequestDto createRequestDto);

    @Mapping(target = "schedule", expression = "java(updateRequestDto.getSchedule().toString())")
    void toDomain(@MappingTarget Course course, CourseUpdateRequestDto updateRequestDto);

    @Mapping(target = "subject", source = "subject.name")
    @Mapping(target = "subjectCode", source = "subject.code")
    @Mapping(target = "program", source = "program.name")
    @Mapping(target = "credits", source = "subject.credits")
    CourseDto toDto(Course entity);

    @Mapping(target = "subject", source = "subject.name")
    @Mapping(target = "subjectCode", source = "subject.code")
    CourseMinimalDto toMinimalDto(Course entity);
}
