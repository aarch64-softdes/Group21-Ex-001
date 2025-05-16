package com.tkpm.sms.infrastructure.persistence.jpa;

import com.tkpm.sms.infrastructure.persistence.entity.StudentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StudentJpaRepository
        extends
            JpaRepository<StudentEntity, String>,
            JpaSpecificationExecutor<StudentEntity> {
    @Query("""
            SELECT DISTINCT st FROM StudentEntity st
            LEFT JOIN FETCH st.mailingAddress ma
            LEFT JOIN FETCH st.permanentAddress pa
            LEFT JOIN FETCH st.temporaryAddress ta
            LEFT JOIN FETCH st.identity i
            WHERE st.id = :studentId
            """)
    Optional<StudentEntity> findByStudentId(String studentId);

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<StudentEntity> findByEmail(String email);

    Optional<StudentEntity> findByPhone(String phone);
}