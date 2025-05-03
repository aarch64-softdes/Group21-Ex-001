package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.application.dto.response.enrollment.AcademicTranscriptDto;
import com.tkpm.sms.application.dto.response.enrollment.TranscriptDto;
import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.Student;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.domain.valueobject.History;
import com.tkpm.sms.infrastructure.persistence.entity.HistoryEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.EnrollmentJpaRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.HistoryJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.EnrollmentPersistenceMapper;
import com.tkpm.sms.infrastructure.persistence.mapper.HistoryPersistenceMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class EnrollmentRepositoryImpl implements EnrollmentRepository {
    EnrollmentJpaRepository enrollmentJpaRepository;
    HistoryJpaRepository historyJpaRepository;
    SettingRepository settingRepository;
    SubjectRepository subjectRepository;

    EnrollmentPersistenceMapper enrollmentPersistenceMapper;
    HistoryPersistenceMapper historyPersistenceMapper;

    @Override
    public PageResponse<Enrollment> findAllEnrollmentsOfStudentWithPaging(String studentId,
            PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageNumber() - 1, pageRequest.getPageSize(),
                pageRequest.getSortDirection() == PageRequest.SortDirection.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                pageRequest.getSortBy());

        var page = enrollmentJpaRepository.findAllEnrollmentOfStudent(studentId, pageable);

        var contents = page.getContent().stream().map(enrollmentPersistenceMapper::toDomain)
                .toList();

        return PageResponse.of(contents, page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public PageResponse<History> findEnrollmentHistoryOfStudent(String studentId,
            PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageNumber() - 1, pageRequest.getPageSize(),
                pageRequest.getSortDirection() == PageRequest.SortDirection.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                pageRequest.getSortBy());

        var page = historyJpaRepository.findAllEnrollmentHistoriesOfStudent(studentId, pageable);

        var contents = page.getContent().stream().map(historyPersistenceMapper::toDomain).toList();

        return PageResponse.of(contents, page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public List<Enrollment> findAllEnrollmentsOfStudent(String studentId) {
        return enrollmentJpaRepository.findAllByStudentId(studentId).stream()
                .map(enrollmentPersistenceMapper::toDomain).toList();
    }

    @Override
    public Optional<Enrollment> findEnrollmentByStudentIdAndCourseId(String studentId,
            Integer courseId) {
        var enrollment = enrollmentJpaRepository.findByStudentIdAndCourseId(studentId, courseId);

        return Optional.ofNullable(enrollment).map(enrollmentPersistenceMapper::toDomain);
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        var enrollmentEntity = enrollmentPersistenceMapper.toEntity(enrollment);
        var savedEnrollmentEntity = enrollmentJpaRepository.save(enrollmentEntity);

        var historyEntity = HistoryEntity.builder().student(enrollmentEntity.getStudent())
                .course(enrollmentEntity.getCourse()).createdAt(LocalDateTime.now())
                .actionType(History.ActionType.ENROLLED.name()).build();
        historyJpaRepository.save(historyEntity);

        return enrollmentPersistenceMapper.toDomain(savedEnrollmentEntity);
    }

    @Override
    public void delete(Enrollment enrollment) {
        var enrollmentEntity = enrollmentPersistenceMapper.toEntity(enrollment);

        var historyEntity = HistoryEntity.builder().student(enrollmentEntity.getStudent())
                .course(enrollmentEntity.getCourse()).createdAt(LocalDateTime.now())
                .actionType(History.ActionType.DELETED.name()).build();
        historyJpaRepository.save(historyEntity);

        enrollmentJpaRepository.delete(enrollmentEntity);
    }

    @Override
    public boolean existsByStudentIdAndCourseId(String studentId, Integer courseId) {
        return enrollmentJpaRepository.existsByStudentIdAndCourseId(studentId, courseId);
    }

    @Override
    public Integer countStudentsByCourseId(Integer courseId) {
        return enrollmentJpaRepository.countAllByCourseId(courseId);
    }

    @Override
    public List<Enrollment> getFailedSubjectsOfStudent(String studentId, List<Integer> subjectIds) {
        var failingGrade = settingRepository.getFailingGradeSetting();
        var enrollments = enrollmentJpaRepository.findAllByStudentId(studentId).stream()
                .filter(enrollmentEntity -> {
                    var subject = enrollmentEntity.getCourse().getSubject();

                    return subjectIds.contains(subject.getId());
                }).toList();
        return enrollments.stream().filter(enrollmentEntity -> {
            var transcript = enrollmentEntity.getScore();
            return Objects.nonNull(transcript.getGpa()) && transcript.getGpa() < failingGrade;
        }).map(enrollmentPersistenceMapper::toDomain).toList();
    }

    @Override
    public List<Enrollment> getUnenrolledOrUnfinishedCourseOfSubjects(String studentId,
            List<Integer> subjectIds) {

        return enrollmentJpaRepository.findAllByStudentId(studentId).stream()
                .filter(enrollmentEntity -> {
                    var subject = enrollmentEntity.getCourse().getSubject();

                    return !subjectIds.contains(subject.getId())
                            || Objects.isNull(enrollmentEntity.getScore().getGpa());
                }).map(enrollmentPersistenceMapper::toDomain).toList();
    }

    @Override
    public boolean existsByCourseId(Integer courseId) {
        return enrollmentJpaRepository.existsByCourseId(courseId);
    }

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
