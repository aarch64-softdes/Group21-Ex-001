package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.infrastructure.persistence.entity.CourseEntity;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    PageResponse<Course> findAll(PageRequest pageRequest);

    Optional<Course> findById(Integer id);

    Course save(Course course);

    void deleteById(Integer id);

    List<CourseEntity> findAllWithSameRoom(String room);

    boolean existsByCodeAndSubjectId(String code, Integer subjectId);

    boolean existsByCodeAndSubjectIdAndIdNot(String code, Integer subjectId, Integer id);
}
