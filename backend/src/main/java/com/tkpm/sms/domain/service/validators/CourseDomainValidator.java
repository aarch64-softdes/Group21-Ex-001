package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.repository.CourseRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CourseDomainValidator {
    private final CourseRepository courseRepository;

    public void validateRoomAndCourseSchedule(String room, String schedule) {
        if (courseRepository.existsByRoomAndCourseSchedule(room, schedule)) {
            throw new DuplicateResourceException(
                    String.format("Course with room %s and schedule %s already exists", room, schedule)
            );
        }
    }

    public void validateRoomAndCourseScheduleForUpdate(Integer id, String room, String schedule) {
        if (courseRepository.existsByIdNotAndRoomAndCourseSchedule(id, room, schedule)) {
            throw new DuplicateResourceException(
                    String.format("Another course with room %s and schedule %s already exists", room, schedule)
            );
        }
    }
}
