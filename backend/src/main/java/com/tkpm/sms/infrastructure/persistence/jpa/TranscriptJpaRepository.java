package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.ScoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TranscriptJpaRepository extends JpaRepository<ScoreEntity, Integer> {

    @Query("SELECT t FROM ScoreEntity t JOIN EnrollmentEntity e WHERE e.id = ?1")
    boolean existsByEnrollmentId(Integer enrollmentId);

    @Query("SELECT t FROM ScoreEntity t JOIN EnrollmentEntity e WHERE e.student.id = ?1")
    List<ScoreEntity> findAllByStudentId(String studentId);
}
