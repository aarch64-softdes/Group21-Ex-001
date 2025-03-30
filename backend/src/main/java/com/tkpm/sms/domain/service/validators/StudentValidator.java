package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.FileProcessingException;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.InvalidStatusTransitionException;
import com.tkpm.sms.domain.exception.InvalidStudentException;
import com.tkpm.sms.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class StudentValidator {
    private final StudentRepository studentRepository;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");

    // Name validation pattern (letters and spaces only)
    private static final Pattern NAME_PATTERN = Pattern.compile("^[\\p{L}\\s]*$");

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

    public void validateEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidStudentException("Invalid email format");
        }
    }

    public void validateName(String name) {
        if (name == null || !NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidStudentException("Name must contain only letters and spaces");
        }
    }

    public void validateEmailDomain(String email, String validDomain) {
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

    public void validateStudentDataForExport(List<Student> students) {
        if (students == null || students.isEmpty()) {
            throw new FileProcessingException("No student data available for export");
        }
    }
}