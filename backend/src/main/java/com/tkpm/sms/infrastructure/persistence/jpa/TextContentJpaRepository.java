package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.TextContentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TextContentJpaRepository extends JpaRepository<TextContentEntity, Integer> {
    @Query("""
            SELECT DISTINCT tc FROM TextContentEntity tc
            LEFT JOIN FETCH tc.translations t
            WHERE tc.id = :id
            """)
    Optional<TextContentEntity> findById(Integer id);
}
