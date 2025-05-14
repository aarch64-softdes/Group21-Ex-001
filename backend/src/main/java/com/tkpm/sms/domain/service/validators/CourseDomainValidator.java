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
import com.tkpm.sms.domain.service.TranslatorService;
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
    TranslatorService translatorService;

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
                throw new DuplicateResourceException("error.course.duplicate_resource.overlapping",
                        course.getRoom(), schedule);
            }
        });
    }

    public void validateCourseInTimePeriod(Course course) {

        var adjustmentDuration = settingRepository.getAdjustmentDurationSetting();

        if (LocalDate.now()
                .isAfter(course.getStartDate().plusDays(Integer.parseInt(adjustmentDuration)))) {
            var expiredFor = LocalDate.now().minusDays(course.getStartDate()
                    .plusDays(Integer.parseInt(adjustmentDuration)).toEpochDay());
            throw new UnenrollableCourseException("error.course.unenrollable.out_of_time",
                    course.getCode(), expiredFor);
        }
    }

    public void validateCourseIsMaxCapacity(Course course) {
        var currentCapacity = enrollmentRepository.countStudentsByCourseId(course.getId());
        if (currentCapacity >= course.getMaxStudent()) {
            throw new UnenrollableCourseException("error.course.unenrollable.full",
                    course.getCode());
        }
    }

    public void validateCodeAndSubject(String code, Integer subjectId) {
        var subject = subjectRepository.findById(subjectId)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found",
                        translatorService.getEntityTranslatedName(Subject.class), subjectId));

        log.info("Validating course code and subject id: code={}, subjectId={}", code, subjectId);
        if (courseRepository.existsByCodeAndSubjectId(code, subjectId)) {
            throw new DuplicateResourceException("error.course.duplicate_resource.code_and_subject",
                    code, subject.getCode());
        }
    }

    public void validateSubjectForCourseUpdate(Integer id, Integer subjectId) {
        if (courseRepository.existsBySubjectIdAndIdNot(subjectId, id)) {
            var course = courseRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("error.not_found",
                            translatorService.getEntityTranslatedName(Course.class), id));
            log.info("Validating subject id for update: subjectCode={}",
                    course.getSubject().getCode());
            throw new DuplicateResourceException("error.course.duplicate_resource.subject",
                    course.getSubject().getCode());
        }
    }

    public void validateEnrollmentExistenceForCourse(Course course) {
        if (enrollmentRepository.existsByCourseId(course.getId())) {
            throw new UnenrollableCourseException("error.course.delete.has_enrollments",
                    course.getCode());
        }
    }
}
