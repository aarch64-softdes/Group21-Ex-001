package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Student;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;



@Repository
public interface StudentRepository extends JpaRepository<Student, String>, JpaSpecificationExecutor<Student> {
    @Query("SELECT s FROM Student s WHERE s.name LIKE %?1% OR s.studentId LIKE %?1%")
    Page<Student> getStudents(String search, Pageable pageable);
    boolean existsStudentByStudentId(String studentId);
    boolean existsStudentByEmail(@Email @NotNull String email);

    boolean existsStudentByPhone(@NotNull @Pattern(regexp = "^0\\d{9}$", message = "Phone number must start with 0 and have 10 digits") String phone);

    void deleteByStudentId(String studentId);
}
