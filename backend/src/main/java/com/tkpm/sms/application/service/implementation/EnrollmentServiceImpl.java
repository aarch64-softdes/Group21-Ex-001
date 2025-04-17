package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.TranscriptUpdateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.AcademicTranscriptDto;
import com.tkpm.sms.application.dto.response.enrollment.TranscriptDto;
import com.tkpm.sms.application.mapper.EnrollmentMapper;
import com.tkpm.sms.application.mapper.TranscriptMapper;
import com.tkpm.sms.application.service.interfaces.CourseService;
import com.tkpm.sms.application.service.interfaces.EnrollmentService;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.application.service.interfaces.SubjectService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.*;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.service.validators.CourseDomainValidator;
import com.tkpm.sms.domain.service.validators.EnrollmentDomainValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;

    private final EnrollmentMapper enrollmentMapper;
    private final TranscriptMapper transcriptMapper;

    private final StudentService studentService;
    private final CourseService courseService;
    private final SubjectService subjectService;

    private final EnrollmentDomainValidator enrollmentDomainValidator;
    private final CourseDomainValidator courseDomainValidator;

    @Override
    public PageResponse<Enrollment> findAllEnrollmentsOfStudent(String studentId, BaseCollectionRequest request) {
        PageRequest pageRequest = PageRequest.from(request);

        // it will throw ResourceNotFoundException when there is no student with studentId-parameter
        studentService.getStudentDetail(studentId);

        return enrollmentRepository.findAllEnrollmentsOfStudentWithPaging(studentId, pageRequest);
    }

    @Override
    public PageResponse<History> findEnrollmentHistoryOfStudent(String studentId, BaseCollectionRequest request) {
        request.setSortBy("createdAt");
        request.setSortDirection("desc");
        PageRequest pageRequest = PageRequest.from(request);

        // it will throw ResourceNotFoundException when there is no student with studentId-parameter
        studentService.getStudentDetail(studentId);

        return enrollmentRepository.findEnrollmentHistoryOfStudent(studentId, pageRequest);
    }

    @Override
    @Transactional
    public Enrollment updateTranscriptOfEnrollment(String studentId, Integer courseId, TranscriptUpdateRequestDto transcriptUpdateRequestDto) {
        Course course = courseService.getCourseById(courseId);
        courseDomainValidator.validateCourseInTimePeriod(course);
        var enrollment = enrollmentRepository.findEnrollmentByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Enrollment not found for studentId: %s and courseId: %s", studentId, courseId)));

        // Course and student will be immutable
        // Only update transcript
        var transcript = transcriptMapper.toTranscript(transcriptUpdateRequestDto);
        enrollment.setTranscript(transcript);

        return enrollmentRepository.save(enrollment);
    }

    @Override

    public Enrollment createEnrollment(EnrollmentCreateRequestDto enrollmentCreateRequestDto) {
        var course = courseService.getCourseById(enrollmentCreateRequestDto.getCourseId());

        courseDomainValidator.validateCourseInTimePeriod(course);
        enrollmentDomainValidator.validateEnrollmentUniqueness(
                enrollmentCreateRequestDto.getStudentId(),
                enrollmentCreateRequestDto.getCourseId());
        courseDomainValidator.validateCourseIsMaxCapacity(course);

        var subject = subjectService.getSubjectById(course.getSubject().getId());

        if (!subject.getPrerequisites().isEmpty()) {
            enrollmentDomainValidator.validateStudentPassedPrerequisitesSubject(
                    enrollmentCreateRequestDto.getStudentId(),
                    subject.getPrerequisites().stream()
                            .map(Subject::getId)
                            .toList()
            );
        }

        Enrollment enrollment = enrollmentMapper.toEnrollment(enrollmentCreateRequestDto);
        enrollment.setStudent(studentService.getStudentDetail(enrollmentCreateRequestDto.getStudentId()));
        enrollment.setCourse(courseService.getCourseById(enrollmentCreateRequestDto.getCourseId()));
        enrollment.setTranscript(new Transcript());

        return enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void deleteEnrollment(EnrollmentDeleteRequestDto enrollmentDeleteRequestDto) {
        var course = courseService.getCourseById(enrollmentDeleteRequestDto.getCourseId());
        courseDomainValidator.validateCourseInTimePeriod(course);

        var enrollment = enrollmentRepository.findEnrollmentByStudentIdAndCourseId(
                enrollmentDeleteRequestDto.getStudentId(),
                enrollmentDeleteRequestDto.getCourseId()
        ).orElseThrow(
                () -> new ResourceNotFoundException(
                        String.format("Enrollment not found for studentId: %s and courseId: %s",
                                enrollmentDeleteRequestDto.getStudentId(),
                                enrollmentDeleteRequestDto.getCourseId()))
        );

        enrollmentRepository.delete(enrollment);
    }

    @Override
    public AcademicTranscriptDto getAcademicTranscriptOfStudent(String studentId) {
        // it will throw ResourceNotFoundException when there is no student with studentId-parameter
        var student = studentService.getStudentDetail(studentId);
        List<Enrollment> enrollments = enrollmentRepository.findAllEnrollmentsOfStudent(studentId);

        var gpa = enrollments.stream()
                .map(Enrollment::getTranscript)
                .map(Transcript::getGpa)
                .reduce(0.0, Double::sum) / enrollments.size();

        var academicTranscript = AcademicTranscriptDto.builder()
                .studentId(student.getId())
                .studentName(student.getName())
                .studentDob(student.getDob())
                .gpa(gpa)
                .courseName(student.getFaculty().getName())
                .transcriptList(
                        enrollments.stream()
                                .map(enrollment -> {
                                    var course = enrollment.getCourse();
                                    var subject = subjectService.getSubjectById(course.getSubject().getId());

                                    return TranscriptDto.builder()
                                            .subjectName(subject.getName())
                                            .subjectCode(subject.getCode())
                                            .grade(enrollment.getTranscript().getGrade())
                                            .gpa(enrollment.getTranscript().getGpa())
                                            .build();
                                })
                                .toList()
                )
                .build();

        return academicTranscript;
    }
}
