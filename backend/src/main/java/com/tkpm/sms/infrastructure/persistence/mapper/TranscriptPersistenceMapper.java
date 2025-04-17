package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Transcript;
import com.tkpm.sms.infrastructure.persistence.entity.TranscriptEntity;
import org.mapstruct.Mapper;

@Mapper
public interface TranscriptPersistenceMapper {
    Transcript toDomain(TranscriptEntity entity);

    TranscriptEntity toEntity(Transcript domain);
}
