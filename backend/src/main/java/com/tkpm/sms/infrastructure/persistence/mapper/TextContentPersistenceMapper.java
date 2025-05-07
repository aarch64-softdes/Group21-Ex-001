package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.valueobject.TextContent;
import com.tkpm.sms.infrastructure.persistence.entity.TextContentEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TranslationPersistenceMapper.class})
public interface TextContentPersistenceMapper {
    TextContent toDomain(TextContentEntity entity);

    TextContentEntity toEntity(TextContent domain);
}
