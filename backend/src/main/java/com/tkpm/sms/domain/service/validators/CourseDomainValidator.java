package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.UnenrollableCourseException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
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
    DomainEntityNameTranslator domainEntityNameTranslator;

    public void validateRoomAndCourseSchedule(Course course) {
        var courses = courseRepository.findAllWithSameRoom(course.getSemester(), course.getYear(),
                course.getRoom());

        courses.forEach(other -> {
            if (other.getId().equals(course.getId())) {
                return;
            }

            var schedule = course.getSchedule();
            var existedSchedule = Schedule.of(other.getSchedule());
            // TODO: creat i18n: error.course.duplicate_resource.overlapping
            if (schedule.isOverlapping(existedSchedule)) {
                // throw new DuplicateResourceException(String.format(
                // "The course with room %s and schedule %s is overlapping other courses.",
                // course.getRoom(), schedule));
                throw new DuplicateResourceException("error.course.duplicate_resource.overlapping",
                        course.getRoom(), schedule);
            }
        });
    }

    public void validateCourseInTimePeriod(Course course) {

        var adjustmentDuration = settingRepository.getAdjustmentDurationSetting();

        if (LocalDate.now()
                .isAfter(course.getStartDate().plusDays(Integer.parseInt(adjustmentDuration)))) {
            // TODO: create i18n: error.course.unenrollable.out_of_time
            var expiredFor = LocalDate.now().minusDays(course.getStartDate()
                    .plusDays(Integer.parseInt(adjustmentDuration)).toEpochDay());
            throw new UnenrollableCourseException("error.course.unenrollable.out_of_time",
                    course.getCode(), expiredFor);
            // throw new UnenrollableCourseException(
            // String.format("Course with code %s can only be modified in %s days",
            // course.getCode(), adjustmentDuration));
        }
    }

    public void validateCourseIsMaxCapacity(Course course) {
        var currentCapacity = enrollmentRepository.countStudentsByCourseId(course.getId());
        if (currentCapacity >= course.getMaxStudent()) {
            // TODO: create i18n: error.course.unenrollable.full
            throw new UnenrollableCourseException("error.course.unenrollable.full",
                    course.getCode());
            // throw new UnenrollableCourseException(
            // String.format("Course with id %s is already full ", course.getId()));
        }
    }

    public void validateCodeAndSubject(String code, Integer subjectId) {
        var subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found",
                        domainEntityNameTranslator.getEntityTranslatedName(Subject.class),
                        subjectId));

        log.info("Validating course code and subject id: code={}, subjectId={}", code, subjectId);
        if (courseRepository.existsByCodeAndSubjectId(code, subjectId)) {
            // TODO: create i18n: error.course.duplicate_resource.code_and_subject
            throw new DuplicateResourceException("error.course.duplicate_resource.code_and_subject",
                    code, subject.getCode());
            // throw new DuplicateResourceException(String.format(
            // "Course with code %s for subject %s already exists", code, subject.getCode()));
        }
    }

    public void validateCodeAndSubjectForUpdate(Integer id, String code, Integer subjectId) {
        if (courseRepository.existsByCodeAndSubjectIdAndIdNot(code, subjectId, id)) {
            var course = courseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("error.not_found",
                            domainEntityNameTranslator.getEntityTranslatedName(Course.class), id));
            log.info("Validating course code and subject id for update: code={}, subjectCode={}",
                    code, course.getSubject().getCode());
            throw new DuplicateResourceException("error.course.duplicate_resource.code_and_subject",
                    code, course.getSubject().getCode());
            // throw new DuplicateResourceException(
            // // TODO: create i18n: error.course.duplicate_resource.code_and_subject
            // String.format("Another course with code %s and subject id %s already exists",
            // code, course.getSubject().getCode()));
        }
    }

    public void validateEnrollmentExistenceForCourse(Course course) {
        if (enrollmentRepository.existsByCourseId(course.getId())) {
            // TODO: create i18n: error.course.delete.has_enrollments
            throw new UnenrollableCourseException("error.course.delete.has_enrollments",
                    course.getCode());
            // throw new DuplicateResourceException(
            // String.format("Course with code %s already has enrollments", course.getCode()));
        }
    }
}
