package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Course;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface CourseRepository {
    PageResponse<Course> findAll(PageRequest pageRequest);

    Optional<Course> findById(String id);

    Course save(Course course);

    void deleteById(String id);

    boolean existsByCourseSchedule(String schedule);

    boolean existsByRoomAndCourseSchedule(String room, String schedule);

    boolean existsByIdNotAndRoomAndCourseSchedule(String id, String room, String schedule);
}
