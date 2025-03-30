package com.tkpm.sms.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.domain.repository.*;
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
    public FacultyDomainValidator facultyValidator(FacultyRepository facultyRepository) {
        return new FacultyDomainValidator(facultyRepository);
    }

    public IdentityDomainValidator identityValidator(IdentityRepository identityRepository) {
        return new IdentityDomainValidator(identityRepository);
    }


    @Bean
    public ProgramDomainValidator programValidator(ProgramRepository programRepository) {
        return new ProgramDomainValidator(programRepository);
    }

    @Bean
    public StudentDomainValidator studentValidator(StudentRepository studentRepository, SettingRepository settingRepository) {
        return new StudentDomainValidator(studentRepository, settingRepository);
    }

    @Bean
    public StatusDomainValidator statusValidator(StatusRepository statusRepository) {
        return new StatusDomainValidator(statusRepository);
    }
}