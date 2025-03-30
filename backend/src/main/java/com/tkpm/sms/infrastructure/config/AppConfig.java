package com.tkpm.sms.infrastructure.config;

import com.tkpm.sms.domain.repository.*;
import com.tkpm.sms.domain.service.validators.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public FacultyDomainValidator facultyValidator(FacultyRepository facultyRepository) {
        return new FacultyDomainValidator(facultyRepository);
    }

    @Bean
    public AddressDomainValidator addressValidator() {
        return new AddressDomainValidator();
    }

    @Bean
    public IdentityDomainValidator identityValidator(IdentityRepository identityRepository) {
        return new IdentityDomainValidator(identityRepository);
    }

    @Bean
    public PhoneNumberValidator phoneValidator() {
        return new PhoneNumberValidator();
    }

    @Bean
    public ProgramDomainValidator programValidator(ProgramRepository programRepository) {
        return new ProgramDomainValidator(programRepository);
    }

    @Bean
    public StudentDomainValidator studentValidator(StudentRepository studentRepository) {
        return new StudentDomainValidator(studentRepository);
    }

    @Bean
    public StatusDomainValidator statusValidator(StatusRepository statusRepository) {
        return new StatusDomainValidator(statusRepository);
    }
}