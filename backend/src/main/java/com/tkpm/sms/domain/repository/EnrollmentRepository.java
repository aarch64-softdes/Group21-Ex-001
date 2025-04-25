package com.tkpm.sms.domain.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.valueobject.History;

import java.util.List;
import java.util.Optional;

public interface EnrollmentRepository {
    PageResponse<Enrollment> findAllEnrollmentsOfStudentWithPaging(String studentId,
            PageRequest pageRequest);

    PageResponse<History> findEnrollmentHistoryOfStudent(String studentId, PageRequest pageRequest);

    List<Enrollment> findAllEnrollmentsOfStudent(String studentId);

    Optional<Enrollment> findEnrollmentByStudentIdAndCourseId(String studentId, Integer courseId);

    Enrollment save(Enrollment enrollment);

    void delete(Enrollment enrollment);

    boolean existsByStudentIdAndCourseId(String studentId, Integer courseId);

    Integer countStudentsByCourseId(Integer courseId);

    List<Enrollment> getFailedSubjectsOfStudent(String studentId, List<Integer> subjectIds);

    List<Enrollment> getUnenrolledOrUnfinishedCourseOfSubjects(String studentId,
            List<Integer> subjectIds);

    boolean existsByCourseId(Integer courseId);
}
