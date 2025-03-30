package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface SettingJpaRepository extends JpaRepository<SettingEntity, String> {
    Optional<SettingEntity> findSettingByName(String name);

    @Query("SELECT s FROM SettingEntity s WHERE s.name = 'phonenumber'")
    SettingEntity findPhoneSetting();

    @Query("SELECT s FROM SettingEntity s WHERE s.name = 'email'")
    SettingEntity findEmailSetting();
}