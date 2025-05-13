package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.StudentPrerequisitesNotSatisfiedException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.StudentRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentDomainValidator {
    EnrollmentRepository enrollmentRepository;
    StudentRepository studentRepository;
    CourseRepository courseRepository;
    DomainEntityNameTranslator domainEntityNameTranslator;

    public void validateEnrollmentUniqueness(String studentId, Integer courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            var student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new ResourceNotFoundException("error.not_found",
                            domainEntityNameTranslator.getEntityTranslatedName(Student.class),
                            studentId));
            var course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new ResourceNotFoundException("error.not_found",
                            domainEntityNameTranslator.getEntityTranslatedName(Course.class),
                            courseId));
            throw new DuplicateResourceException(
                    "error.enrollment.duplicate_resource.student_and_course",
                    student.getStudentId(), course.getCode());
        }
    }

    public void validateStudentSatisfiedPrerequisitesSubject(String studentId,
            List<Integer> subjectIds) {

        var languageCode = LocaleContextHolder.getLocale().getLanguage();
        var missingEnrollments = enrollmentRepository
                .getUnenrolledOrUnfinishedCourseOfSubjects(studentId, subjectIds);
        var student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found",
                        domainEntityNameTranslator.getEntityTranslatedName(Student.class),
                        studentId));

        if (!missingEnrollments.isEmpty()) {
            List<String> subjects = missingEnrollments.stream().map(enrollment -> enrollment
                    .getCourse().getSubject().getNameByLanguage(languageCode)).toList();

            throw new StudentPrerequisitesNotSatisfiedException(
                    "error.enrollment.student_prerequisite_not_met.has_not_finished",
                    student.getStudentId(), String.join(", ", subjects));
        }

        var failedSubjects = enrollmentRepository.getFailedSubjectsOfStudent(studentId, subjectIds);
        if (!failedSubjects.isEmpty()) {
            var failedSubjectNames = failedSubjects.stream().map(enrollment -> enrollment
                    .getCourse().getSubject().getNameByLanguage(languageCode)).toList();

            throw new StudentPrerequisitesNotSatisfiedException(
                    "error.enrollment.student_prerequisite_not_met.has_not_passed",
                    student.getStudentId(), String.join(", ", failedSubjectNames));
        }
    }
}
