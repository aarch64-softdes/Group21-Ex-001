package com.tkpm.sms.domain.repository;


import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Transcript;

import java.util.List;

public interface TranscriptRepository {
    /**
     * Find all transcripts by student ID.
     * For example, if the student ID is "12345", it will find all transcripts for that student.
     * Purpose: support for exporting academic transcript.
     */
    List<Transcript> findAllByStudentId(String studentId);

    Transcript save(Transcript transcript);

    boolean existsByEnrollmentId(Integer enrollmentId);
}
