package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Address;
import com.tkpm.sms.infrastructure.persistence.entity.AddressEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressPersistenceMapper {

    AddressEntity toEntity(Address domain);

    Address toDomain(AddressEntity entity);

    void updateEntityFromDomain(Address domain, @MappingTarget AddressEntity entity);

    void updateDomainFromEntity(AddressEntity entity, @MappingTarget Address domain);
}