package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.EnrollmentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EnrollmentJpaRepository extends JpaRepository<EnrollmentEntity, Integer> {
    @Query("select e from EnrollmentEntity e where e.student.id = ?1")
    Page<EnrollmentEntity> findAllEnrollmentOfStudent(String studentId, Pageable pageable);

    EnrollmentEntity findByStudent_IdAndCourse_Id(String studentId, Integer courseId);

    boolean existsByStudent_IdAndCourse_Id(String studentId, Integer courseId);

    Integer countAllByCourse_Id(Integer courseId);
}
