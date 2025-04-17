package com.tkpm.sms.domain.repository;


import com.tkpm.sms.domain.model.Transcript;
import java.util.List;

public interface TranscriptRepository {

    List<Transcript> findAllByStudentId(String studentId);

    Transcript save(Transcript transcript);

    boolean existsByEnrollmentId(Integer enrollmentId);
}
