package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.IdentityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdentityJpaRepository extends JpaRepository<IdentityEntity, String> {
    boolean existsByNumberAndType(String number, String type);
    boolean existsByNumberAndTypeAndIdNot(String number, String type, String id);
}