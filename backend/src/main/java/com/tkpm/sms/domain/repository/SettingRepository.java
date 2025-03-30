package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.model.Setting;

import java.util.Optional;

public interface SettingRepository {
    Setting save(Setting setting);
    Optional<Setting> findByName(String name);
}