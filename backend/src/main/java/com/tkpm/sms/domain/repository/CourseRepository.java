package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Course;

import java.util.List;
import java.util.Optional;

public interface CourseRepository {
    PageResponse<Course> findAll(PageRequest pageRequest);

    Optional<Course> findById(Integer id);

    Course save(Course course);

    void deleteById(Integer id);

    List<Course> findAllWithSameRoom(int semester, int year, String room);

    boolean existsByCodeAndSubjectId(String code, Integer subjectId);

    boolean existsByCodeAndSubjectIdAndIdNot(String code, Integer subjectId, Integer id);

    boolean existsBySubjectIdAndIdNot(Integer subjectId, Integer id);
}
