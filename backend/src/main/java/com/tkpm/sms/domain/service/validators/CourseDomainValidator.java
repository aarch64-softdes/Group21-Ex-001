package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.UnenrollableCourseException;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.History;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.SettingRepository;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

    public void validateCodeAndSubject(String code, Integer subjectId) {
        var course = courseRepository.findById(subjectId).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Course with id %s not found", subjectId)
                )
        );

        log.info("Validating course code and subject id: code={}, subjectId={}", code, subjectId);
        if (courseRepository.existsByCodeAndSubjectId(code, subjectId)) {
            throw new DuplicateResourceException(
                    String.format("Course with code %s for subject %s already exists", code, course.getSubject().getCode())
            );
        }
    }

    public void validateCodeAndSubjectForUpdate(Integer id, String code, Integer subjectId) {
        if (courseRepository.existsByCodeAndSubjectIdAndIdNot(code, subjectId, id)) {
            var course = courseRepository.findById(id).orElseThrow(
                    () -> new ResourceNotFoundException(
                            String.format("Course with id %s not found", id)
                    )
            );
            log.info("Validating course code and subject id for update: code={}, subjectCode={}", code, course.getSubject().getCode());
            throw new DuplicateResourceException(
                    String.format("Another course with code %s and subject id %s already exists", code, course.getSubject().getCode())
            );
        }
    }
}
