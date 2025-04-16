package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.CourseExpiredException;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.SettingRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class CourseDomainValidator {
    private final CourseRepository courseRepository;
    private final SettingRepository settingRepository;

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

    public void validateCourseInTimePeriod(Integer id) {
        var course = courseRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(String.format("Course with id %s not found", id))
        );

        var adjustmentDuration = settingRepository.getAdjustmentDurationSetting();

        if(LocalDate.now().isAfter(course.getStartDate().plusDays(Integer.parseInt(adjustmentDuration)))) {
            throw new CourseExpiredException(
                    String.format("Course with id %s is expired", id)
            );
        }
    }
}
