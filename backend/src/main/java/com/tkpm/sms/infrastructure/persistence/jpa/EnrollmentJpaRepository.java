package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.EnrollmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;

public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Integer> {
    @Query("""
            SELECT DISTINCT e FROM EnrollmentEntity e
            LEFT JOIN FETCH e.student st
            LEFT JOIN FETCH e.course c LEFT JOIN FETCH e.score sc
            WHERE st.id = :studentId
            """)
    Page<EnrollmentEntity> findAllEnrollmentOfStudent(String studentId, Pageable pageable);

    EnrollmentEntity findByStudentIdAndCourseId(String studentId, Integer courseId);

    boolean existsByStudentIdAndCourseId(String studentId, Integer courseId);

    @Query("SELECT COUNT(e) FROM EnrollmentEntity e WHERE e.course.id = :courseId AND e.student.deletedAt IS NULL")
    Integer countAllByCourseId(Integer courseId);

    Collection<EnrollmentEntity> findAllByStudentId(String studentId);

    boolean existsByCourseId(Integer courseId);
}
