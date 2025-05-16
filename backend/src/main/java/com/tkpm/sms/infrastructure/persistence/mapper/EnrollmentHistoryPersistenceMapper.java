package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.valueobject.EnrollmentHistory;
import com.tkpm.sms.application.mapper.ScheduleMapper;
import com.tkpm.sms.infrastructure.persistence.entity.EnrollmentHistoryEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {StudentPersistenceMapper.class, ScheduleMapper.class})
public interface EnrollmentHistoryPersistenceMapper {
    EnrollmentHistory toDomain(EnrollmentHistoryEntity enrollmentHistoryEntity);

    EnrollmentHistoryEntity toEntity(EnrollmentHistory enrollmentHistory);
}
