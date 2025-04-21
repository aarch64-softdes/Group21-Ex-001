package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.subject.SubjectCreateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectUpdateRequestDto;
import com.tkpm.sms.application.dto.response.subject.PrerequisiteSubjectDto;
import com.tkpm.sms.application.dto.response.subject.SubjectDto;
import com.tkpm.sms.application.mapper.SubjectMapper;
import com.tkpm.sms.domain.model.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SubjectMapperImpl extends SubjectMapper {

    @Override
    @Mapping(target = "faculty", ignore = true)
    Subject toSubject(SubjectCreateRequestDto subjectRequestDto);

    @Override
    @Mapping(target = "faculty", source = "faculty")
    @Mapping(target = "isActive", source = "active")
    SubjectDto toSubjectDto(Subject subject);

    @Override
    @Mapping(target = "prerequisites", ignore = true)
    void updateSubjectFromDto(@MappingTarget Subject subject,
            SubjectUpdateRequestDto subjectRequestDto);

    @Override
    PrerequisiteSubjectDto toPrerequisiteSubjectDto(Subject subject);
}
