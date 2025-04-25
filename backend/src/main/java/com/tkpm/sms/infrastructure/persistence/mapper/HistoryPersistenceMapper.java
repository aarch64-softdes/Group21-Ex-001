package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.valueobject.History;
import com.tkpm.sms.infrastructure.persistence.entity.HistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StudentPersistenceMapper.class,})
public interface HistoryPersistenceMapper {
    History toDomain(HistoryEntity historyEntity);

    HistoryEntity toEntity(History history);
}
