package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.common.PageRequest;
import com.tkpm.sms.domain.common.PageResponse;
import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.domain.repository.StatusRepository;
import com.tkpm.sms.infrastructure.persistence.entity.StatusEntity;
import com.tkpm.sms.infrastructure.persistence.jpa.StatusJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.StatusPersistenceMapper;
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
public class StatusRepositoryImpl implements StatusRepository {
    private final StatusJpaRepository jpaRepository;
    private final StatusPersistenceMapper mapper;

    @Override
    public Status save(Status status) {
        var entity = mapper.toEntity(status);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Status> findById(Integer id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Status> findByName(String name) {
        return jpaRepository.findStatusByName(name)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsStatusByName(name);
    }

    @Override
    public boolean existsByNameAndIdNot(String name, Integer id) {
        return jpaRepository.existsStatusByNameAndIdNot(name, id);
    }

    @Override
    public boolean existsByFromStatusIdAndToStatusId(Integer fromStatusId, Integer toStatusId) {
        return jpaRepository.existsByFromStatusIdAndToStatusId(fromStatusId, toStatusId);
    }

    @Override
    public PageResponse<Status> findAll(PageRequest pageRequest) {
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
        Page<StatusEntity> page = jpaRepository.findAll(pageable);

        // Convert Spring Page to domain PageResponse
        List<Status> content = page.getContent().stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());

        return PageResponse.of(
                content,
                page.getNumber() + 1, // Convert 0-based to 1-based
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}