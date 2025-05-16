package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.valueobject.Translation;
import com.tkpm.sms.infrastructure.persistence.entity.TranslationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TranslationPersistenceMapper {
    @Mapping(target = "isOriginal", source = "original")
    Translation toDomain(TranslationEntity entity);

    @Mapping(target = "isOriginal", source = "original")
    TranslationEntity toEntity(Translation domain);
}
