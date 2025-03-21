package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.response.StatusDto;
import com.tkpm.sms.entity.Status;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface StatusMapper {
    StatusDto toStatusDto(Status status);

    Status toStatus(StatusDto statusDto);
}
