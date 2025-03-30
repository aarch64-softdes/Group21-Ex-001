package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    Student save(Student student);
    void delete(Student student);
    Optional<Student> findById(String id);
    Optional<Student> findByEmail(String email);
    Optional<Student> findByStudentId(String studentId);
    boolean existsByStudentId(String studentId);
    boolean existsByEmail(String email);
    PageResponse<Student> findAll(PageRequest pageRequest);
    PageResponse<Student> findWithFilters(String search, String faculty, PageRequest pageRequest);
    List<Student> findAll();
}