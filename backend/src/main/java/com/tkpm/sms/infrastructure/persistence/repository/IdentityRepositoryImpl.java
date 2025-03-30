package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.repository.IdentityRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.IdentityJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.IdentityPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class IdentityRepositoryImpl implements IdentityRepository {
    private final IdentityJpaRepository jpaRepository;
    private final IdentityPersistenceMapper mapper;

    @Override
    public Identity save(Identity identity) {
        var entity = mapper.toEntity(identity);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Identity> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public boolean existsByNumberAndType(String number, IdentityType type) {
        return jpaRepository.existsByNumberAndType(number, type.getDisplayName());
    }

    @Override
    public boolean existsByNumberAndTypeAndIdNot(String number, IdentityType type, String id) {
        return jpaRepository.existsByNumberAndTypeAndIdNot(number, type.getDisplayName(), id);
    }
}