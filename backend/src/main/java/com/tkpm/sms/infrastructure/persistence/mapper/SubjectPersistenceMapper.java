package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Subject;
import com.tkpm.sms.infrastructure.persistence.entity.SubjectEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        FacultyPersistenceMapper.class
})
public interface SubjectPersistenceMapper {
    @Mapping(target = "isActive", source = "active")
    Subject toDomain(SubjectEntity entity);

    @Mapping(target = "isActive", source = "active")
    SubjectEntity toEntity(Subject domain);
}
