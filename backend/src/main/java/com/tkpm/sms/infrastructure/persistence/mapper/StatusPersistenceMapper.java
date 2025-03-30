package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Status;
import com.tkpm.sms.infrastructure.persistence.entity.StatusEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StatusPersistenceMapper {

    @Mapping(target = "students", ignore = true)
    Status toDomain(StatusEntity entity);

    @Mapping(target = "students", ignore = true)
    StatusEntity toEntity(Status domain);

    @Mapping(target = "students", ignore = true)
    void updateDomainFromEntity(StatusEntity entity, @MappingTarget Status domain);

    @Mapping(target = "students", ignore = true)
    void updateEntityFromDomain(Status domain, @MappingTarget StatusEntity entity);
}