package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.TextContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TextContentJpaRepository extends JpaRepository<TextContentEntity, Integer> {
}
