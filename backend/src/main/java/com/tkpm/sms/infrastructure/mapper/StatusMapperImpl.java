package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.application.dto.response.status.StatusDto;
import com.tkpm.sms.application.mapper.StatusMapper;
import com.tkpm.sms.domain.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.i18n.LocaleContextHolder;

@Mapper(componentModel = "spring", imports = {LocaleContextHolder.class})
public interface StatusMapperImpl extends StatusMapper {

    @Override
    @Mapping(target = "name", expression = "java(status.getNameByLanguage(LocaleContextHolder.getLocale().getLanguage()))")
    StatusDto toStatusDto(Status status);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "name", ignore = true)
    Status toStatus(StatusRequestDto statusRequestDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "students", ignore = true)
    @Mapping(target = "name", ignore = true)
    void updateStatusFromDto(StatusRequestDto statusRequestDto, @MappingTarget Status status);
}