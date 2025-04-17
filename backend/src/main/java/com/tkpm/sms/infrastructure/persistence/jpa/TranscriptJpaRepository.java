package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.domain.repository.TranscriptRepository;
import com.tkpm.sms.infrastructure.persistence.entity.TranscriptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface TranscriptJpaRepository extends JpaRepository<TranscriptEntity, Integer> {

    boolean existsByEnrollmentId(Integer enrollmentId);

    @Query("SELECT t FROM TranscriptEntity t JOIN EnrollmentEntity e WHERE e.student.id = ?1")
    List<TranscriptEntity> findAllByStudentId(String studentId);
}
