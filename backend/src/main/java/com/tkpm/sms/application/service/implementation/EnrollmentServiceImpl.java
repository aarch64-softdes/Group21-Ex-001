package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.common.BaseCollectionRequest;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentCreateRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentDeleteRequestDto;
import com.tkpm.sms.application.dto.request.enrollment.EnrollmentFileImportDto;
import com.tkpm.sms.application.dto.request.enrollment.TranscriptUpdateRequestDto;
import com.tkpm.sms.application.dto.response.enrollment.AcademicTranscriptDto;
import com.tkpm.sms.application.mapper.EnrollmentMapper;
import com.tkpm.sms.application.mapper.ScoreMapper;
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
import com.tkpm.sms.domain.valueobject.History;
import com.tkpm.sms.domain.valueobject.Score;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentServiceImpl implements EnrollmentService {
    EnrollmentRepository enrollmentRepository;

    EnrollmentMapper enrollmentMapper;
    ScoreMapper scoreMapper;

    StudentService studentService;
    CourseService courseService;
    SubjectService subjectService;

    EnrollmentDomainValidator enrollmentDomainValidator;
    CourseDomainValidator courseDomainValidator;

    @Override
    public PageResponse<Enrollment> findAllEnrollmentsOfStudent(String studentId,
            BaseCollectionRequest request, String languageCode) {
        PageRequest pageRequest = PageRequest.from(request);

        // it will throw ResourceNotFoundException when there is no student with studentId-parameter
        studentService.getStudentDetail(studentId, languageCode);

        return enrollmentRepository.findAllEnrollmentsOfStudentWithPaging(studentId, pageRequest);
    }

    @Override
    public PageResponse<History> findEnrollmentHistoryOfStudent(String studentId,
            BaseCollectionRequest request, String languageCode) {
        request.setSortBy("createdAt");
        request.setSortDirection("desc");
        PageRequest pageRequest = PageRequest.from(request);

        // it will throw ResourceNotFoundException when there is no student with studentId-parameter
        studentService.getStudentDetail(studentId, languageCode);

        return enrollmentRepository.findEnrollmentHistoryOfStudent(studentId, pageRequest);
    }

    @Override
    @Transactional
    public void updateTranscriptOfEnrollment(String studentId, Integer courseId,
            TranscriptUpdateRequestDto transcriptUpdateRequestDto) {
        Course course = courseService.getCourseById(courseId);
        courseDomainValidator.validateCourseInTimePeriod(course);

        // TODO: Language Support
        Student student = studentService.getStudentDetail(studentId, "en");

        var enrollment = enrollmentRepository
                .findEnrollmentByStudentIdAndCourseId(studentId, courseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Student %s is not enrolled into course %s",
                                student.getStudentId(), course.getCode())));

        var score = scoreMapper.toScore(transcriptUpdateRequestDto);
        enrollment.setScore(score);

        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void updateTranscripts(List<EnrollmentFileImportDto> enrollmentFileImportDtos) {
        enrollmentFileImportDtos.forEach(enrollmentFileImportDto -> {
            var transcriptUpdateDto = TranscriptUpdateRequestDto.builder()
                    .grade(enrollmentFileImportDto.getGrade().trim())
                    .gpa(enrollmentFileImportDto.getGpa()).build();

            updateTranscriptOfEnrollment(enrollmentFileImportDto.getStudentId(),
                    enrollmentFileImportDto.getCourseId(), transcriptUpdateDto);
        });
    }

    @Override
    @Transactional
    public Enrollment createEnrollment(EnrollmentCreateRequestDto enrollmentCreateRequestDto,
            String languageCode) {
        var course = courseService.getCourseById(enrollmentCreateRequestDto.getCourseId());

        courseDomainValidator.validateCourseInTimePeriod(course);
        enrollmentDomainValidator.validateEnrollmentUniqueness(
                enrollmentCreateRequestDto.getStudentId(),
                enrollmentCreateRequestDto.getCourseId());
        courseDomainValidator.validateCourseIsMaxCapacity(course);

        var subject = subjectService.getSubjectById(course.getSubject().getId());

        if (!subject.getPrerequisites().isEmpty()) {
            enrollmentDomainValidator.validateStudentSatisfiedPrerequisitesSubject(
                    enrollmentCreateRequestDto.getStudentId(),
                    subject.getPrerequisites().stream().map(Subject::getId).toList());
        }

        Enrollment enrollment = enrollmentMapper.toEnrollment(enrollmentCreateRequestDto);
        enrollment.setStudent(studentService
                .getStudentDetail(enrollmentCreateRequestDto.getStudentId(), languageCode));
        enrollment.setCourse(courseService.getCourseById(enrollmentCreateRequestDto.getCourseId()));
        enrollment.setScore(new Score());

        return enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional
    public void deleteEnrollment(EnrollmentDeleteRequestDto enrollmentDeleteRequestDto) {
        var course = courseService.getCourseById(enrollmentDeleteRequestDto.getCourseId());
        courseDomainValidator.validateCourseInTimePeriod(course);

        var enrollment = enrollmentRepository
                .findEnrollmentByStudentIdAndCourseId(enrollmentDeleteRequestDto.getStudentId(),
                        enrollmentDeleteRequestDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Enrollment not found for studentId: %s and courseId: %s",
                                enrollmentDeleteRequestDto.getStudentId(),
                                enrollmentDeleteRequestDto.getCourseId())));

        enrollmentRepository.delete(enrollment);
    }

    @Override
    @Transactional
    public AcademicTranscriptDto getAcademicTranscriptOfStudent(String studentId,
            String languageCode) {
        // it will throw ResourceNotFoundException when there is no student with studentId-parameter
        var student = studentService.getStudentDetail(studentId, languageCode);
        List<Enrollment> enrollments = enrollmentRepository.findAllEnrollmentsOfStudent(studentId)
                .stream()
                .filter(enrollment -> enrollment.getScore() != null
                        && enrollment.getScore().getGrade() != null
                        && !enrollment.getScore().getGrade().isEmpty())
                .toList();

        return enrollmentRepository.getAcademicTranscript(student, enrollments);
    }

}
