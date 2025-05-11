package com.tkpm.sms.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.domain.repository.*;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
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
            DomainEntityNameTranslator domainEntityNameTranslator) {
        return new FacultyDomainValidator(facultyRepository, domainEntityNameTranslator);
    }

    @Bean
    public IdentityDomainValidator identityValidator(IdentityRepository identityRepository,
            DomainEntityNameTranslator domainEntityNameTranslator) {
        return new IdentityDomainValidator(identityRepository, domainEntityNameTranslator);
    }

    @Bean
    public ProgramDomainValidator programValidator(ProgramRepository programRepository,
            DomainEntityNameTranslator domainEntityNameTranslator) {
        return new ProgramDomainValidator(programRepository, domainEntityNameTranslator);
    }

    @Bean
    public StudentDomainValidator studentValidator(StudentRepository studentRepository,
            SettingRepository settingRepository,
            DomainEntityNameTranslator domainEntityNameTranslator) {
        return new StudentDomainValidator(studentRepository, settingRepository,
                domainEntityNameTranslator);
    }

    @Bean
    public StatusDomainValidator statusValidator(StatusRepository statusRepository,
            DomainEntityNameTranslator domainEntityNameTranslator) {
        return new StatusDomainValidator(statusRepository, domainEntityNameTranslator);
    }

    @Bean
    public CourseDomainValidator courseValidator(CourseRepository courseRepository,
            SubjectRepository subjectRepository, SettingRepository settingRepository,
            EnrollmentRepository enrollmentRepository,
            DomainEntityNameTranslator domainEntityNameTranslator) {
        return new CourseDomainValidator(courseRepository, subjectRepository, settingRepository,
                enrollmentRepository, domainEntityNameTranslator);
    }

    @Bean
    public SubjectDomainValidator subjectValidator(SubjectRepository subjectRepository,
            DomainEntityNameTranslator domainEntityNameTranslator) {
        return new SubjectDomainValidator(subjectRepository, domainEntityNameTranslator);
    }

    @Bean
    public EnrollmentDomainValidator enrollmentValidator(EnrollmentRepository enrollmentRepository,
            StudentRepository studentRepository, CourseRepository courseRepository,
            DomainEntityNameTranslator domainEntityNameTranslator) {
        return new EnrollmentDomainValidator(enrollmentRepository, studentRepository,
                courseRepository, domainEntityNameTranslator);
    }
}