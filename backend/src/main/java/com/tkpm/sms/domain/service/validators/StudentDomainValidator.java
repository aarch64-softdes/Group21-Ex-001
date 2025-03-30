package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.InvalidStatusTransitionException;
import com.tkpm.sms.domain.exception.InvalidStudentException;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class StudentDomainValidator {
    private final StudentRepository studentRepository;
    private final SettingRepository settingRepository;

    public void validateStudentIdUniqueness(String studentId) {
        if (studentRepository.existsByStudentId(studentId)) {
            throw new DuplicateResourceException(
                    String.format("Student with ID %s already exists", studentId)
            );
        }
    }

    public void validateStudentIdUniquenessForUpdate(String studentId, String id) {
        Optional<Student> existingStudent = studentRepository.findByStudentId(studentId);
        if (existingStudent.isPresent() && !existingStudent.get().getId().equals(id)) {
            throw new DuplicateResourceException(
                    String.format("Student with ID %s already exists", studentId)
            );
        }
    }

    public void validateEmailUniqueness(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException(
                    String.format("Student with email %s already exists", email)
            );
        }
    }

    public void validateEmailUniquenessForUpdate(String email, String id) {
        Optional<Student> existingStudent = studentRepository.findByEmail(email);
        if (existingStudent.isPresent() && !existingStudent.get().getId().equals(id)) {
            throw new DuplicateResourceException(
                    String.format("Student with email %s already exists", email)
            );
        }
    }

    public void validateEmailDomain(String email) {
        var validDomain = settingRepository.getEmailSetting();

        if (!email.endsWith(validDomain)) {
            throw new InvalidStudentException(
                    String.format("Email domain is not supported, only %s is allowed", validDomain)
            );
        }
    }

    public void validateStatusTransition(Student student, Status newStatus) {
        if (student.getStatus() != null && !student.isStatusTransitionAllowed(newStatus)) {
            throw new InvalidStatusTransitionException(
                    String.format("Transition from %s to %s is not allowed",
                            student.getStatus().getName(), newStatus.getName())
            );
        }
    }
}