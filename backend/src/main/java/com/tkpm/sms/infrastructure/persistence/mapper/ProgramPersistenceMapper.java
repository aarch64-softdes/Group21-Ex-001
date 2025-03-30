package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Program;
import com.tkpm.sms.infrastructure.persistence.entity.ProgramEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ProgramPersistenceMapper {

    @Mapping(target = "students", ignore = true)
    Program toDomain(ProgramEntity entity);

    @Mapping(target = "students", ignore = true)
    ProgramEntity toEntity(Program domain);

    @Mapping(target = "students", ignore = true)
    Program toDomain(ProgramEntity entity, @MappingTarget Program domain);

    @Mapping(target = "students", ignore = true)
    ProgramEntity toEntity(Program domain, @MappingTarget ProgramEntity entity);
}