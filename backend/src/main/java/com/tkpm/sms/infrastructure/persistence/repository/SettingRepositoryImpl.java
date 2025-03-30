package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.SettingJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.SettingPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SettingRepositoryImpl implements SettingRepository {
    private final SettingJpaRepository jpaRepository;
    private final SettingPersistenceMapper mapper;

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
}