package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.application.dto.response.enrollment.AcademicTranscriptDto;
import com.tkpm.sms.application.dto.response.enrollment.TranscriptDto;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.repository.TranscriptRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TranscriptRepositoryImpl implements TranscriptRepository {
    SubjectRepository subjectRepository;

    @Override
    public AcademicTranscriptDto getAcademicTranscript(Student student,
            List<Enrollment> enrollments) {

        return AcademicTranscriptDto.builder().studentId(student.getId())
                .studentName(student.getName()).studentDob(student.getDob())
                .gpa(getTotalGpa(enrollments)).courseName(student.getFaculty().getName())
                .transcriptList(enrollments.stream().map(enrollment -> {
                    var course = enrollment.getCourse();
                    var subject = subjectRepository.findById(course.getSubject().getId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    String.format("Subject with id %s cannot be found",
                                            course.getSubject().getId())));

                    return TranscriptDto.builder().subjectName(subject.getName())
                            .subjectCode(subject.getCode()).grade(enrollment.getScore().getGrade())
                            .gpa(enrollment.getScore().getGpa()).build();
                }).toList()).build();
    }

    private Double getTotalGpa(List<Enrollment> enrollments) {
        if (enrollments.isEmpty()) {
            return 0.0;
        }

        double gpa = enrollments.stream().mapToDouble(enrollment -> {
            var score = enrollment.getScore();
            return score.getGpa() * enrollment.getCourse().getSubject().getCredits();
        }).sum() / enrollments.stream()
                .mapToDouble(enrollment -> enrollment.getCourse().getSubject().getCredits()).sum();

        return Math.ceil(gpa * 100.0) / 100.0;
    }
}
