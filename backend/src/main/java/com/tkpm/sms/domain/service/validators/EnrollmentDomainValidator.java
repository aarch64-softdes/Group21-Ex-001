package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.StudentPrerequisitesNotSatisfiedException;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentDomainValidator {
    private final EnrollmentRepository enrollmentRepository;

    public void validateEnrollmentUniqueness(String studentId, Integer courseId) {
        if (enrollmentRepository.existsByStudentIdAndCourseId(studentId, courseId)) {
            throw new DuplicateResourceException("Enrollment already existed for studentId: " + studentId +
                    " and courseId: " + courseId);
        }
    }

    public void validateStudentPassedPrerequisitesSubject(String studentId, List<Integer> subjectIds) {
        if (!enrollmentRepository.isStudentPassedSubjects(studentId, subjectIds)) {
            throw new StudentPrerequisitesNotSatisfiedException("This student has not passed this course yet.");
        }
    }
}
