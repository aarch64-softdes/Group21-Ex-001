package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.SettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SettingJpaRepository extends JpaRepository<SettingEntity, Integer> {
    Optional<SettingEntity> findSettingByName(String name);

    @Query("SELECT s FROM SettingEntity s WHERE s.name = 'phonenumber'")
    SettingEntity findPhoneSetting();

    @Query("SELECT s FROM SettingEntity s WHERE s.name = 'email'")
    SettingEntity findEmailSetting();

    @Query("SELECT s FROM SettingEntity s WHERE s.name = 'adjustmentduration'")
    SettingEntity findAdjustmentDurationSetting();

    @Query("SELECT s FROM SettingEntity s WHERE s.name = 'failinggrade'")
    SettingEntity findFailingGradeSetting();
}