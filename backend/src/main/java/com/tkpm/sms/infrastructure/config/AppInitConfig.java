package com.tkpm.sms.infrastructure.config;

import com.tkpm.sms.infrastructure.persistence.entity.SettingEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.SettingJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Slf4j
@Configuration
public class AppInitConfig {
    private final Map<String, String> defaultSettings = Map.of("email", "@student.hcmus.edu.vn",
            "phonenumber", "[\"VN\"]", "adjustmentduration", "10", "failinggrade", "2.0");

    @Bean
    ApplicationRunner applicationRunner(SettingJpaRepository settingJpaRepository) {
        return args -> {
            for (var entry : defaultSettings.entrySet()) {
                String name = entry.getKey();
                if (settingJpaRepository.findSettingByName(name).isPresent()) {
                    continue;
                }
                String value = entry.getValue();

                if (settingJpaRepository.findSettingByName(name).isEmpty()) {
                    SettingEntity settingEntity = new SettingEntity();
                    settingEntity.setName(name);
                    settingEntity.setDetails(value);
                    settingJpaRepository.save(settingEntity);
                    log.warn("Default setting {} has been created with value: {}", name, value);
                }
            }
        };
    }
}
