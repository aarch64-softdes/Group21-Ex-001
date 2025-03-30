package com.tkpm.sms.infrastructure.config;

import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.domain.service.validators.FacultyValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Bean
    public FacultyValidator facultyDomainService(
            FacultyRepository facultyRepository
    ) {
        return new FacultyValidator(facultyRepository);
    }
}
