package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Score;
import com.tkpm.sms.infrastructure.persistence.entity.ScoreEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScorePersistenceMapper {
    Score toDomain(ScoreEntity entity);

    ScoreEntity toEntity(Score domain);
}
