package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.application.dto.response.status.StatusDto;
import com.tkpm.sms.application.mapper.StatusMapper;
import com.tkpm.sms.domain.model.Status;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface StatusMapperImpl extends StatusMapper {

    StatusDto toStatusDto(Status status);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "students", ignore = true)
    Status toStatus(StatusRequestDto statusRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deletedAt", ignore = true)
    @Mapping(target = "students", ignore = true)
    void updateStatusFromDto(StatusRequestDto statusRequestDto, @MappingTarget Status status);
}