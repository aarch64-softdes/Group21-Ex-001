package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.domain.repository.FacultyRepository;
import com.tkpm.sms.infrastructure.persistence.entity.FacultyEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.FacultyJpaRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.TranslationJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.FacultyPersistenceMapper;
import com.tkpm.sms.infrastructure.utils.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class FacultyRepositoryImpl implements FacultyRepository {
    private final FacultyJpaRepository facultyJpaRepository;
    private final TranslationJpaRepository translationJpaRepository;

    private final FacultyPersistenceMapper facultyMapper;
    private final FacultyPersistenceMapper facultyPersistenceMapper;

    @Override
    public Faculty save(Faculty faculty) {
        var entity = facultyMapper.toEntity(faculty);
        var savedEntity = facultyJpaRepository.save(entity);
        return facultyMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Faculty> findById(Integer id) {
        return facultyJpaRepository.findById(id).map(facultyMapper::toDomain);
    }

    @Override
    public Optional<Faculty> findByName(String name) {
        return facultyJpaRepository.findFacultyByName(name).map(facultyMapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return facultyJpaRepository.existsFacultyByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return facultyJpaRepository.existsByNameAndIdNot(name, id);
    }

    @Override
    public void deleteById(Integer id) {
        facultyJpaRepository.deleteById(id);
    }

    @Override
    public PageResponse<Faculty> findAll(PageRequest pageRequest) {
        // Convert domain PageRequest to Spring Pageable
        Pageable pageable = PagingUtils.toSpringPageable(pageRequest);

        // Execute query with Spring Pageable
        Page<FacultyEntity> page = facultyJpaRepository.findAll(pageable);

        // Convert Spring Page to domain PageResponse
        List<Faculty> content = page.getContent().stream().map(facultyMapper::toDomain)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getNumber() + 1, page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public void delete(Faculty faculty) {
        var entity = facultyMapper.toEntity(faculty);
        facultyJpaRepository.delete(entity);
    }
}