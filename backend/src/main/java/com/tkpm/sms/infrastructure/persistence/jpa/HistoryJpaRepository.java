package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.HistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HistoryJpaRepository extends JpaRepository<HistoryEntity, String> {
    @Query("select h from HistoryEntity h where h.student.id = ?1")
    Page<HistoryEntity> findAllEnrollmentHistoriesOfStudent(String id, Pageable pageable);
}
