package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Course;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface CourseRepository {
    PageResponse<Course> findAll(PageRequest pageRequest);

    Optional<Course> findById(Integer id);

    Course save(Course course);

    void deleteById(Integer id);

    boolean existsByCourseSchedule(String schedule);

    boolean existsByRoomAndCourseSchedule(String room, String schedule);

    boolean existsByIdNotAndRoomAndCourseSchedule(Integer id, String room, String schedule);
}
