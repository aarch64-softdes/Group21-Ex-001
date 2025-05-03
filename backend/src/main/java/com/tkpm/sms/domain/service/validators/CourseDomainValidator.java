package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.UnenrollableCourseException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.valueobject.Schedule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CourseDomainValidator {
    CourseRepository courseRepository;
    SubjectRepository subjectRepository;
    SettingRepository settingRepository;
    EnrollmentRepository enrollmentRepository;

    public void validateRoomAndCourseSchedule(Course course) {
        var courses = courseRepository.findAllWithSameRoom(course.getSemester(), course.getYear(),
                course.getRoom());

        courses.forEach(other -> {
            if (other.getId().equals(course.getId())) {
                return;
            }

            var schedule = course.getSchedule();
            var existedSchedule = Schedule.of(other.getSchedule().toString());
            if (schedule.isOverlapping(existedSchedule)) {
                throw new DuplicateResourceException(String.format(
                        "The course with room %s and schedule %s is overlapping other courses.",
                        course.getRoom(), schedule));
            }
        });
    }

    public void validateCourseInTimePeriod(Course course) {

        var adjustmentDuration = settingRepository.getAdjustmentDurationSetting();

        if (LocalDate.now()
                .isAfter(course.getStartDate().plusDays(Integer.parseInt(adjustmentDuration)))) {
            throw new UnenrollableCourseException(
                    String.format("Course %s can only be modified in %s days", course.getCode(),
                            adjustmentDuration));
        }
    }

    public void validateCourseIsMaxCapacity(Course course) {
        var currentCapacity = enrollmentRepository.countStudentsByCourseId(course.getId());
        if (currentCapacity >= course.getMaxStudent()) {
            throw new UnenrollableCourseException(
                    String.format("Course %s is already full ", course.getCode()));
        }
    }

    public void validateCodeAndSubject(String code, Integer subjectId) {
        var subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Subject with id %s not found", subjectId)));

        log.info("Validating course code and subject id: code={}, subjectId={}", code, subjectId);
        if (courseRepository.existsByCodeAndSubjectId(code, subjectId)) {
            throw new DuplicateResourceException(String
                    .format("Course %s for subject %s already exists", code, subject.getCode()));
        }
    }

    public void validateCodeAndSubjectForUpdate(Integer id, String code, Integer subjectId) {
        if (courseRepository.existsByCodeAndSubjectIdAndIdNot(code, subjectId, id)) {
            var course = courseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Course with id %s not found", id)));
            log.info("Validating course code and subject id for update: code={}, subjectCode={}",
                    code, course.getSubject().getCode());
            throw new DuplicateResourceException(
                    String.format("Another course with code %s and subject id %s already exists",
                            code, course.getSubject().getCode()));
        }
    }

    public void validateEnrollmentExistenceForCourse(Course course) {
        if (enrollmentRepository.existsByCourseId(course.getId())) {
            throw new DuplicateResourceException(
                    String.format("Course %s already has enrollments", course.getCode()));
        }
    }
}
