package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.course.CourseCreateRequestDto;
import com.tkpm.sms.application.dto.request.course.CourseUpdateRequestDto;
import com.tkpm.sms.application.dto.response.course.CourseDto;
import com.tkpm.sms.application.dto.response.course.CourseMinimalDto;
import com.tkpm.sms.application.mapper.CourseMapper;
import com.tkpm.sms.application.mapper.ProgramMapper;
import com.tkpm.sms.application.mapper.ScheduleMapper;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.domain.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {SubjectMapper.class, ProgramMapper.class,
        ScheduleMapper.class})
public interface CourseMapperImpl extends CourseMapper {
    Course toDomain(CourseCreateRequestDto createRequestDto);

    void toDomain(@MappingTarget Course course, CourseUpdateRequestDto updateRequestDto);

    @Mapping(target = "credits", source = "subject.credits")
    CourseDto toDto(Course entity);

    @Mapping(target = "subject", source = "subject.name")
    @Mapping(target = "subjectCode", source = "subject.code")
    @Mapping(target = "code", source = "code")
    CourseMinimalDto toMinimalDto(Course entity);
}
