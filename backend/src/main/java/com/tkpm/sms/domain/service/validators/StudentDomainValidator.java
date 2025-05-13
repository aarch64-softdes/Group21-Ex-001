package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.UnsupportedStatusTransitionException;
import com.tkpm.sms.domain.exception.UnsupportedEmailException;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.service.TranslatorService;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.domain.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Optional;

@RequiredArgsConstructor
public class StudentDomainValidator {
    private final StudentRepository studentRepository;
    private final SettingRepository settingRepository;
    private final TranslatorService translatorService;

    public void validateStudentIdUniqueness(String studentId) {
        if (studentRepository.existsByStudentId(studentId)) {
            throw new DuplicateResourceException("error.student.duplicate_resource.student_id",
                    translatorService.getEntityTranslatedName(Student.class), studentId);
            // throw new DuplicateResourceException(
            // String.format("Student with ID %s already exists", studentId));
        }
    }

    public void validateStudentIdUniquenessForUpdate(String studentId, String id) {
        Optional<Student> existingStudent = studentRepository.findByStudentId(studentId);
        if (existingStudent.isPresent() && !existingStudent.get().getId().equals(id)) {
            throw new DuplicateResourceException("error.student.duplicate_resource.student_id",
                    translatorService.getEntityTranslatedName(Student.class), studentId);
        }
    }

    public void validateEmailUniqueness(String email) {
        if (studentRepository.existsByEmail(email)) {
            throw new DuplicateResourceException("error.student.duplicate_resource.email",
                    translatorService.getEntityTranslatedName(Student.class), email);
        }
    }

    public void validateEmailUniquenessForUpdate(String email, String id) {
        Optional<Student> existingStudent = studentRepository.findByEmail(email);
        if (existingStudent.isPresent() && !existingStudent.get().getId().equals(id)) {
            throw new DuplicateResourceException("error.student.duplicate_resource.email",
                    translatorService.getEntityTranslatedName(Student.class), email);
        }
    }

    public void validateEmailDomain(String email) {
        var validDomain = settingRepository.getEmailSetting();

        if (!email.endsWith(validDomain)) {
            throw new UnsupportedEmailException("error.student.invalid_email_domain",
                    translatorService.getEntityTranslatedName(Student.class), email, validDomain);
        }
    }

    public void validateStatusTransition(Student student, Status newStatus) {
        if (student.getStatus() != null && !student.isStatusTransitionAllowed(newStatus)) {
            var fromStatus = student.getStatus()
                    .getNameByLanguage(LocaleContextHolder.getLocale().getLanguage());
            var toStatus = newStatus
                    .getNameByLanguage(LocaleContextHolder.getLocale().getLanguage());

            throw new UnsupportedStatusTransitionException(
                    "error.status.unsupported_status_transition", fromStatus, toStatus);
        }
    }
}