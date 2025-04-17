package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.exception.StudentPrerequisitesNotSatisfiedException;
import com.tkpm.sms.domain.model.Enrollment;
import com.tkpm.sms.domain.model.History;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.EnrollmentRepository;
import com.tkpm.sms.domain.repository.SettingRepository;
import com.tkpm.sms.domain.repository.SubjectRepository;
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
    public PageResponse<Enrollment> findAllEnrollmentsOfStudentWithPaging(String studentId, PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageNumber() - 1,
                pageRequest.getPageSize(),
                pageRequest.getSortDirection() == PageRequest.SortDirection.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                pageRequest.getSortBy()
        );

        var page = enrollmentJpaRepository.findAllEnrollmentOfStudent(studentId, pageable);

        var contents = page.getContent().stream()
                .map(enrollmentPersistenceMapper::toDomain)
                .toList();

        return PageResponse.of(
                contents,
                page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public PageResponse<History> findEnrollmentHistoryOfStudent(String studentId, PageRequest pageRequest) {
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageNumber() - 1,
                pageRequest.getPageSize(),
                pageRequest.getSortDirection() == PageRequest.SortDirection.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                pageRequest.getSortBy()
        );

        var page = historyJpaRepository.findAllEnrollmentHistoriesOfStudent(studentId, pageable);

        var contents = page.getContent().stream()
                .map(historyPersistenceMapper::toDomain)
                .toList();

        return PageResponse.of(
                contents,
                page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public List<Enrollment> findAllEnrollmentsOfStudent(String studentId) {
        return enrollmentJpaRepository.findAllByStudentId(studentId).stream()
                .map(enrollmentPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Enrollment> findEnrollmentByStudentIdAndCourseId(String studentId, Integer courseId) {
        var enrollment = enrollmentJpaRepository.findByStudentIdAndCourseId(studentId, courseId);

        return Optional.ofNullable(enrollment)
                .map(enrollmentPersistenceMapper::toDomain);
    }

    @Override
    public Enrollment save(Enrollment enrollment) {
        var enrollmentEntity = enrollmentPersistenceMapper.toEntity(enrollment);
        var savedEnrollmentEntity = enrollmentJpaRepository.save(enrollmentEntity);

        var historyEntity = HistoryEntity.builder().
                student(enrollmentEntity.getStudent()).
                course(enrollmentEntity.getCourse()).
                createdAt(LocalDateTime.now()).
                actionType(History.ActionType.ENROLLED.name()).
                build();
        historyJpaRepository.save(historyEntity);

        return enrollmentPersistenceMapper.toDomain(savedEnrollmentEntity);
    }

    @Override
    public void delete(Enrollment enrollment) {
        var enrollmentEntity = enrollmentPersistenceMapper.toEntity(enrollment);

        var historyEntity = HistoryEntity.builder().
                student(enrollmentEntity.getStudent()).
                course(enrollmentEntity.getCourse()).
                createdAt(LocalDateTime.now()).
                actionType(History.ActionType.DELETED.name()).
                build();
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
    public boolean isStudentPassedSubjects(String studentId, List<Integer> subjectIds) {
        var failingGrade = settingRepository.getFailingGradeSetting();

        var enrollments = enrollmentJpaRepository.findAllByStudentId(studentId)
                .stream().filter(
                        enrollmentEntity -> {
                            var subject = enrollmentEntity.getCourse().getSubject();

                            return subjectIds.contains(subject.getId());
                        }
                ).toList();

        if (enrollments.size() != subjectIds.size()) {
            var missingSubjectIds = subjectIds.stream()
                    .filter(subjectId -> enrollments.stream()
                            .noneMatch(enrollmentEntity -> enrollmentEntity.getCourse().getSubject().getId().equals(subjectId)))
                    .toList();

            var subjects = subjectRepository.findAllByIds(missingSubjectIds).stream().map(
                    Subject::getCode
            ).toList();

            throw new StudentPrerequisitesNotSatisfiedException("Student has not finished the following subjects: " + String.join(", ", subjects));
        }

        return enrollments.stream()
                .allMatch(enrollmentEntity -> {
                    var transcript = enrollmentEntity.getScore();
                    return Objects.nonNull(transcript.getGpa()) && transcript.getGpa() >= failingGrade;
                });
    }
}
