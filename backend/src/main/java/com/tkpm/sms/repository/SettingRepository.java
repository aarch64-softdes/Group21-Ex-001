package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, String> {
    Optional<Setting> findSettingByName(String name);
}
