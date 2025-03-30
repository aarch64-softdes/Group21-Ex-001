package com.tkpm.sms.infrastructure.persistence.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.SettingJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.SettingPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SettingRepositoryImpl implements SettingRepository {
    private final SettingJpaRepository jpaRepository;
    private final SettingPersistenceMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public Setting save(Setting setting) {
        var entity = mapper.toEntity(setting);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Setting> findByName(String name) {
        return jpaRepository.findSettingByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public List<String> getPhoneSetting()  {
        var setting = jpaRepository.findPhoneSetting();

        var allowedCountries = setting.getDetails();

        try {
            return objectMapper.readValue(allowedCountries, List.class);
        } catch (JsonProcessingException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public String getEmailSetting() {
        var setting = jpaRepository.findEmailSetting();

        return setting.getDetails();
    }
}