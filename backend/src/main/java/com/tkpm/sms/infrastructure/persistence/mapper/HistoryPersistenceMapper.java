package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.application.mapper.ScheduleMapper;
import com.tkpm.sms.domain.model.History;
import com.tkpm.sms.infrastructure.persistence.entity.HistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StudentPersistenceMapper.class, ScheduleMapper.class})
public interface HistoryPersistenceMapper {
    History toDomain(HistoryEntity historyEntity);

    HistoryEntity toEntity(History history);
}
