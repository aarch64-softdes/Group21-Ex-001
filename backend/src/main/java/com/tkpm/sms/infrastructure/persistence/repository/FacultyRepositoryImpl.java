package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.infrastructure.persistence.entity.FacultyEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.FacultyJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.FacultyPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FacultyRepositoryImpl implements FacultyRepository {
    private final FacultyJpaRepository jpaRepository;
    private final FacultyPersistenceMapper mapper;

    @Override
    public Faculty save(Faculty faculty) {
        var entity = mapper.toEntity(faculty);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Faculty> findById(Integer id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Faculty> findByName(String name) {
        return jpaRepository.findFacultyByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsFacultyByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return jpaRepository.existsByNameAndIdNot(name, id);
    }

    @Override
    public void deleteById(Integer id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public PageResponse<Faculty> findAll(PageRequest pageRequest) {
        // Convert domain PageRequest to Spring Pageable
        Pageable pageable = org.springframework.data.domain.PageRequest.of(
                pageRequest.getPageNumber() - 1,
                pageRequest.getPageSize(),
                pageRequest.getSortDirection() == PageRequest.SortDirection.DESC
                        ? Sort.Direction.DESC
                        : Sort.Direction.ASC,
                pageRequest.getSortBy()
        );

        // Execute query with Spring Pageable
        Page<FacultyEntity> page = jpaRepository.findAll(pageable);

        // Convert Spring Page to domain PageResponse
        List<Faculty> content = page.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        return PageResponse.of(
                content,
                page.getNumber() + 1,
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}