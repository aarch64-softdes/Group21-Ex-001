package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.EnrollmentHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface EnrollmentHistoryJpaRepository extends JpaRepository<EnrollmentHistoryEntity, String> {
    @Query("select h from EnrollmentHistoryEntity h where h.student.id = ?1")
    Page<EnrollmentHistoryEntity> findAllEnrollmentHistoriesOfStudent(String id, Pageable pageable);
}
