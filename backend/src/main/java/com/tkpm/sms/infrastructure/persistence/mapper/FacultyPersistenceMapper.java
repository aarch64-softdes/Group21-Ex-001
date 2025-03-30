package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Faculty;
import com.tkpm.sms.infrastructure.persistence.entity.FacultyEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FacultyPersistenceMapper {
    @Mapping(target = "students", ignore = true)
    Faculty toDomain(FacultyEntity entity);

    @Mapping(target = "students", ignore = true)
    FacultyEntity toEntity(Faculty domain);
}