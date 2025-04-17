package com.tkpm.sms.domain.repository;


import com.tkpm.sms.domain.model.Score;

import java.util.List;

public interface TranscriptRepository {

    List<Score> findAllByStudentId(String studentId);

    Score save(Score score);

    boolean existsByEnrollmentId(Integer enrollmentId);
}
