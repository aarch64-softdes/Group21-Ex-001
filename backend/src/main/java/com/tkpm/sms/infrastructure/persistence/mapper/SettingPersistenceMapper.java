package com.tkpm.sms.infrastructure.persistence.mapper;

import com.tkpm.sms.domain.model.Setting;
import com.tkpm.sms.infrastructure.persistence.entity.SettingEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SettingPersistenceMapper {
    Setting toDomain(SettingEntity entity);
    SettingEntity toEntity(Setting domain);
}