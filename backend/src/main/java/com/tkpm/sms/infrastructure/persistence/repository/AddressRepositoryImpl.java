package com.tkpm.sms.infrastructure.persistence.repository;

import com.tkpm.sms.domain.model.Address;
import com.tkpm.sms.domain.repository.AddressRepository;
import com.tkpm.sms.infrastructure.persistence.jpa.AddressJpaRepository;
import com.tkpm.sms.infrastructure.persistence.mapper.AddressPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AddressRepositoryImpl implements AddressRepository {
    private final AddressJpaRepository jpaRepository;
    private final AddressPersistenceMapper mapper;

    @Override
    public Address save(Address address) {
        var entity = mapper.toEntity(address);
        var savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Address> findById(String id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }
}