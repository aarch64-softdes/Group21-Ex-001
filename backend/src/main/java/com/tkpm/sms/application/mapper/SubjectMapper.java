package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.subject.SubjectCreateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectUpdateRequestDto;
import com.tkpm.sms.application.dto.response.subject.SubjectDto;
import com.tkpm.sms.domain.model.Subject;
import org.mapstruct.MappingTarget;

public interface SubjectMapper {
    Subject toSubject(SubjectCreateRequestDto subjectRequestDto);

    SubjectDto toSubjectDto(Subject subject);

    void updateSubjectFromDto(@MappingTarget Subject subject, SubjectUpdateRequestDto subjectRequestDto);
}
