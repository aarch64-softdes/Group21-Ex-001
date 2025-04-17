package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.TranscriptUpdateRequestDto;
import com.tkpm.sms.application.mapper.EnrollmentMapper;
import com.tkpm.sms.application.service.interfaces.CourseService;
import com.tkpm.sms.application.service.interfaces.EnrollmentService;
import com.tkpm.sms.application.service.interfaces.StudentService;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Course;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.History;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.StudentRepository;
import com.tkpm.sms.domain.service.validators.CourseDomainValidator;
import com.tkpm.sms.domain.service.validators.EnrollmentDomainValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final StudentService studentService;
    private final CourseService courseService;
    private final EnrollmentDomainValidator enrollmentDomainValidator;
    private final CourseDomainValidator courseDomainValidator;

    @Override
    public PageResponse<Enrollment> findAllEnrollmentsOfStudent(String studentId, BaseCollectionRequest request) {
        PageRequest pageRequest = PageRequest.from(request);

        // it will throw ResourceNotFoundException when there is no student with studentId-parameter
        studentService.getStudentDetail(studentId);

        return enrollmentRepository.findAllEnrollmentsOfStudent(studentId, pageRequest);
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

        return null;
    }

    @Override

    public Enrollment createEnrollment(EnrollmentCreateRequestDto enrollmentCreateRequestDto) {
        var course = courseService.getCourseById(enrollmentCreateRequestDto.getCourseId());
        courseDomainValidator.validateCourseInTimePeriod(course);
        enrollmentDomainValidator.validateEnrollmentUniqueness(
                enrollmentCreateRequestDto.getStudentId(),
                enrollmentCreateRequestDto.getCourseId());
        courseDomainValidator.validateCourseIsMaxCapacity(course);

        Enrollment enrollment = enrollmentMapper.toEnrollment(enrollmentCreateRequestDto);
        enrollment.setStudent(studentService.getStudentDetail(enrollmentCreateRequestDto.getStudentId()));
        enrollment.setCourse(courseService.getCourseById(enrollmentCreateRequestDto.getCourseId()));

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
}
