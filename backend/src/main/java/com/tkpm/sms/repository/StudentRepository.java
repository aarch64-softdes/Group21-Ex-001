package com.tkpm.sms.repository;

import com.tkpm.sms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, String> {
    @Query("SELECT s FROM Student s WHERE s.name LIKE %?1% OR s.studentId LIKE %?1%")
    List<Student> getStudentsByNameOrId(String find);
}
