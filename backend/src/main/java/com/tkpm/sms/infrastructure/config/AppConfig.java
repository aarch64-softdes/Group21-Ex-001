package com.tkpm.sms.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.domain.repository.*;
import com.tkpm.sms.domain.service.TranslatorService;
import com.tkpm.sms.domain.service.validators.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    public FacultyDomainValidator facultyValidator(FacultyRepository facultyRepository,
            TranslatorService translatorService) {
        return new FacultyDomainValidator(facultyRepository, translatorService);
    }

    @Bean
    public IdentityDomainValidator identityValidator(IdentityRepository identityRepository,
            TranslatorService translatorService) {
        return new IdentityDomainValidator(identityRepository, translatorService);
    }

    @Bean
    public ProgramDomainValidator programValidator(ProgramRepository programRepository,
            TranslatorService translatorService) {
        return new ProgramDomainValidator(programRepository, translatorService);
    }

    @Bean
    public StudentDomainValidator studentValidator(StudentRepository studentRepository,
            SettingRepository settingRepository, TranslatorService translatorService) {
        return new StudentDomainValidator(studentRepository, settingRepository, translatorService);
    }

    @Bean
    public StatusDomainValidator statusValidator(StatusRepository statusRepository,
            TranslatorService translatorService) {
        return new StatusDomainValidator(statusRepository, translatorService);
    }

    @Bean
    public CourseDomainValidator courseValidator(CourseRepository courseRepository,
            SubjectRepository subjectRepository, SettingRepository settingRepository,
            EnrollmentRepository enrollmentRepository, TranslatorService translatorService) {
        return new CourseDomainValidator(courseRepository, subjectRepository, settingRepository,
                enrollmentRepository, translatorService);
    }

    @Bean
    public SubjectDomainValidator subjectValidator(SubjectRepository subjectRepository,
            TranslatorService translatorService) {
        return new SubjectDomainValidator(subjectRepository, translatorService);
    }

    @Bean
    public EnrollmentDomainValidator enrollmentValidator(EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository, CourseRepository courseRepository,
            TranslatorService translatorService) {
        return new EnrollmentDomainValidator(enrollmentRepository, studentRepository,
                courseRepository, translatorService);
    }
}