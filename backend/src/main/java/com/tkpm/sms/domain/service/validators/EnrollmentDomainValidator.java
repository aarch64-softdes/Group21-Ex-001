package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.StudentPrerequisitesNotSatisfiedException;
import com.tkpm.sms.domain.repository.CourseRepository;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.StudentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.List;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentDomainValidator {
    EnrollmentRepository enrollmentRepository;
    StudentRepository studentRepository;
    CourseRepository courseRepository;

    public void validateEnrollmentUniqueness(String studentId, Integer courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            var student = studentRepository.findById(studentId).orElseThrow(
                    () -> new ResourceNotFoundException("Student not found: " + studentId)
            );
            var course = courseRepository.findById(courseId).orElseThrow(
                    () -> new ResourceNotFoundException("Course not found: " + courseId)
            );
            throw new DuplicateResourceException("Enrollment already existed for student: " + student.getStudentId() +
                    " and course: " + course.getCode());
        }
    }

    public void validateStudentSatisfiedPrerequisitesSubject(String studentId, List<Integer> subjectIds) {

        var missingEnrollments = enrollmentRepository.getUnenrolledCourseOfSubjects(studentId, subjectIds);
        if (!missingEnrollments.isEmpty()) {
            List<String> subjects = subjectIds.stream()
                    .map(subjectId -> enrollmentRepository.findEnrollmentByStudentIdAndCourseId(studentId, subjectId)
                            .orElseThrow(() -> new StudentPrerequisitesNotSatisfiedException("Subject not found for student: " + studentId)))
                    .map(subject -> subject.getCourse().getSubject().getName())
                    .toList();

            throw new StudentPrerequisitesNotSatisfiedException("Student has not finished the following subjects: " + String.join(", ",
                    missingEnrollments.stream().map(
                            enrollment -> enrollment.getCourse().getSubject().getName()).toList()));
        }

        var failedSubjects = enrollmentRepository.getFailedSubjectsOfStudent(studentId, subjectIds);
        if (!failedSubjects.isEmpty()) {
            throw new StudentPrerequisitesNotSatisfiedException("Student has not passed these following prerequisite subjects: "
                    + failedSubjects.stream().map(enrollment -> enrollment.getCourse().getSubject().getName())
                    .toList());
        }
    }
}
