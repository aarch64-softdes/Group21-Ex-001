package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingJpaRepository extends JpaRepository<SettingEntity, String> {
    Optional<SettingEntity> findSettingByName(String name);
}