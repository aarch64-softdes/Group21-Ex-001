package com.tkpm.sms.domain.repository;


import com.tkpm.sms.application.dto.response.enrollment.AcademicTranscriptDto;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.valueobject.Score;

import java.util.List;

public interface TranscriptRepository {

    AcademicTranscriptDto getAcademicTranscript(Student student, List<Enrollment> enrollments);
}
