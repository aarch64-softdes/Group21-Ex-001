package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.domain.repository.SubjectRepository;
import com.tkpm.sms.infrastructure.persistence.entity.SubjectEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.SubjectJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.SubjectPersistenceMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SubjectRepositoryImpl implements SubjectRepository {
    private final SubjectJpaRepository jpaRepository;
    private final SubjectPersistenceMapper mapper;

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }

    @Override
    public Optional<Subject> findById(Integer id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public PageResponse<Subject> findAll(PageRequest request) {
        // Convert domain PageRequest to Spring Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                request.getPageNumber() - 1,
                request.getPageSize(),
                request.getSortDirection() == PageRequest.SortDirection.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                request.getSortBy()
        );

        Page<SubjectEntity> page = jpaRepository.findAll(pageable);

        // Convert Spring Page to domain PageResponse
        List<Subject> subjects = page.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        return PageResponse.of(
                subjects,
                page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    @Override
    public PageResponse<Subject> findWithFilters(String search, String faculty, PageRequest request) {
        return null;
    }

    @Override
    public Subject save(Subject subject) {
        SubjectEntity subjectEntity = mapper.toEntity(subject);

        SubjectEntity savedEntity = jpaRepository.save(subjectEntity);

        return mapper.toDomain(savedEntity);
    }

    @Override
    public void delete(Subject subject) {
        SubjectEntity subjectEntity = mapper.toEntity(subject);

        jpaRepository.delete(subjectEntity);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return jpaRepository.existsByNameAndIdNot(name, id);
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, Integer id) {
        return jpaRepository.existsByCodeAndIdNot(code, id);
    }

    @Override
    public boolean isPrerequisiteForOtherSubjects(Integer subjectId) {
        return jpaRepository.isPrerequisitesForOtherSubjects(subjectId);
    }

    @Override
    public boolean existsCourseForSubject(Integer subjectId) {
        return jpaRepository.existsCourseForSubject(subjectId);
    }
}
