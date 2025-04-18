package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.EnrollmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Collection;

public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Integer> {
    @Query("select e from EnrollmentEntity e where e.student.id = ?1")
    Page<EnrollmentEntity> findAllEnrollmentOfStudent(String studentId, Pageable pageable);

    EnrollmentEntity findByStudentIdAndCourseId(String studentId, Integer courseId);

    boolean existsByStudentIdAndCourseId(String studentId, Integer courseId);

    Integer countAllByCourseId(Integer courseId);

    Collection<EnrollmentEntity> findAllByStudentId(String studentId);

    boolean existsByCourseId(Integer courseId);
}
