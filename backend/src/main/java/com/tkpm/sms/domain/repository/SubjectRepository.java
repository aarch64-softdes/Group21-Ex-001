package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Subject;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository {
    boolean existsByCode(String code);

    Optional<Subject> findById(Integer id);

    PageResponse<Subject> findAll(PageRequest request);

    List<Subject> findAllByIds(Iterable<Integer> ids);

    PageResponse<Subject> findWithFilters(String search, String faculty, PageRequest request);

    Subject save(Subject subject);

    void delete(Subject subject);

    boolean existsByName(String name);

    boolean existsByNameAndIdNot(String name, Integer id);

    boolean existsByCodeAndIdNot(String code, Integer id);

    boolean isPrerequisiteForOtherSubjects(Integer subjectId);

    boolean existsCourseForSubject(Integer subjectId);
}
