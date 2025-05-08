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
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(componentModel = "spring", uses = {FacultyMapperImpl.class,
        ProgramMapperImpl.class}, imports = {LocaleContextHolder.class})
public interface SubjectMapperImpl extends SubjectMapper {

    @Override
    @Mapping(target = "faculty", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    Subject toSubject(SubjectCreateRequestDto subjectRequestDto);

    @Override
    @Mapping(target = "isActive", source = "active")
    @Mapping(target = "name", expression = "java(subject.getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    @Mapping(target = "description", expression = "java(subject.getDescriptionByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    SubjectDto toSubjectDto(Subject subject);

    @Override
    @Mapping(target = "prerequisites", ignore = true)
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    void updateSubjectFromDto(@MappingTarget Subject subject,
            SubjectUpdateRequestDto subjectRequestDto);

    @Override
    @Mapping(target = "name", ignore = true)
    @Mapping(target = "description", ignore = true)
    PrerequisiteSubjectDto toPrerequisiteSubjectDto(Subject subject);
}
