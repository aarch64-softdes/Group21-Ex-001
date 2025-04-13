package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.subject.SubjectRequestDto;
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
    Subject toSubject(SubjectRequestDto subjectRequestDto);

    @Override
    SubjectDto toSubjectDto(Subject subject);

    @Override
    void updateSubjectFromDto(SubjectRequestDto subjectRequestDto, @MappingTarget Subject subject);
}
