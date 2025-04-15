package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.History;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    PageResponse<Enrollment> findAllEnrollmentsOfStudent(String studentId, PageRequest pageRequest);

    PageResponse<History> findEnrollmentHistoryOfStudent(String studentId, PageRequest pageRequest);

    Optional<Enrollment> findEnrollmentByStudentIdAndCourseId(String studentId, Integer courseId);

    Enrollment save(Enrollment enrollment);

    void delete(Enrollment enrollment);

    boolean existsByStudentIdAndCourseId(String studentId, Integer courseId);
}
