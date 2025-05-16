package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.TranslationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TranslationJpaRepository extends JpaRepository<TranslationEntity, Integer> {
    TranslationEntity findByTextAndLanguageCode(String text, String languageCode);
}
