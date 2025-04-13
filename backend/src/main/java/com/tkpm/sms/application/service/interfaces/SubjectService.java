package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.subject.SubjectCreateRequestDto;
import com.tkpm.sms.application.dto.request.subject.SubjectUpdateRequestDto;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Subject;

public interface SubjectService {
    PageResponse<Subject> findAll(BaseCollectionRequest request);

    Subject getSubjectById(Integer id);

    Subject createSubject(SubjectCreateRequestDto subjectRequestDto);

    Subject updateSubject(Integer id, SubjectUpdateRequestDto updateRequestDto);

    void deleteSubject(Integer id);
}
