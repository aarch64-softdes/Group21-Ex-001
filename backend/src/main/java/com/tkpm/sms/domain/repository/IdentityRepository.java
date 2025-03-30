package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.model.Identity;

import java.util.Optional;

public interface IdentityRepository {
    Identity save(Identity identity);
    Optional<Identity> findById(String id);
    boolean existsByNumberAndType(String number, IdentityType type);
    boolean existsByNumberAndTypeAndIdNot(String number, IdentityType type, String id);
}