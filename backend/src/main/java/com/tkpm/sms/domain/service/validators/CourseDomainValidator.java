package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.UnenrollableCourseException;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.History;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.SettingRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@RequiredArgsConstructor
public class CourseDomainValidator {
    private final CourseRepository courseRepository;
    private final SettingRepository settingRepository;
    private final EnrollmentRepository enrollmentRepository;

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

    public void validateCourseInTimePeriod(Course course) {

        var adjustmentDuration = settingRepository.getAdjustmentDurationSetting();

        if(LocalDate.now().isAfter(course.getStartDate().plusDays(Integer.parseInt(adjustmentDuration)))) {
            throw new UnenrollableCourseException(
                    String.format("Course with id %s is expired", course.getId())
            );
        }
    }

    public void validateCourseIsMaxCapacity(Course course) {
        var currentCapacity = enrollmentRepository.countStudentsByCourseId(course.getId());
        if(currentCapacity >= course.getMaxStudent()){
            throw new UnenrollableCourseException(
                    String.format("Course with id %s is already full ", course.getId())
            );
        }
    }
}
