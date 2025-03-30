package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.infrastructure.persistence.entity.IdentityEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IdentityPersistenceMapper {

    @Mapping(target = "type", source = "type", qualifiedByName = "typeEnumToString")
    IdentityEntity toEntity(Identity domain);

    @Mapping(target = "type", source = "type", qualifiedByName = "typeStringToEnum")
    Identity toDomain(IdentityEntity entity);

    @Named("typeEnumToString")
    default String typeEnumToString(IdentityType type) {
        return type != null ? type.getDisplayName() : null;
    }

    @Named("typeStringToEnum")
    default IdentityType typeStringToEnum(String type) {
        return type != null ? IdentityType.fromDisplayName(type) : null;
    }
}