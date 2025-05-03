package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.domain.repository.ProgramRepository;
import com.tkpm.sms.infrastructure.persistence.entity.ProgramEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.ProgramJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.ProgramPersistenceMapper;
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
public class ProgramRepositoryImpl implements ProgramRepository {
    private final ProgramJpaRepository jpaRepository;
    private final ProgramPersistenceMapper mapper;

    @Override
    public Program save(Program program) {
        var entity = mapper.toEntity(program);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Program> findById(Integer id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Program> findByName(String name) {
        return jpaRepository.findProgramByName(name).map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsProgramByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return jpaRepository.existsProgramByNameAndIdNot(name, id);
    }

    @Override
    public PageResponse<Program> findAll(PageRequest pageRequest) {
        // Convert domain PageRequest to Spring Pageable
        Pageable pageable = PagingUtils.toSpringPageable(pageRequest);

        // Execute query with Spring Pageable
        Page<ProgramEntity> page = jpaRepository.findAll(pageable);

        // Convert Spring Page to domain PageResponse
        List<Program> content = page.getContent().stream().map(mapper::toDomain)
                .collect(Collectors.toList());

        return PageResponse.of(content, page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(), page.getTotalElements(), page.getTotalPages());
    }

    @Override
    public void delete(Program program) {
        var entity = mapper.toEntity(program);
        jpaRepository.delete(entity);
    }
}