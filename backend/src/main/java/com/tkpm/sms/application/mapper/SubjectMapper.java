package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.subject.SubjectRequestDto;
import com.tkpm.sms.application.dto.response.subject.SubjectDto;
import com.tkpm.sms.domain.model.Subject;

public interface SubjectMapper {
    Subject toSubject(SubjectRequestDto subjectRequestDto);

    SubjectDto toSubjectDto(Subject subject);

    void updateSubjectFromDto(SubjectRequestDto subjectRequestDto, Subject subject);
}
