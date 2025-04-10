package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.subject.SubjectRequestDto;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Subject;

import java.util.List;

public interface SubjectService {
    PageResponse<Subject> findAll(BaseCollectionRequest request);

    Subject getSubjectById(Integer id);

    Subject createSubject(SubjectRequestDto subjectRequestDto);

    Subject updateSubject(Integer id, SubjectRequestDto subjectRequestDto);

    void deleteSubject(Integer id);
}
