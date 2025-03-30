package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.status.StatusRequestDto;
import com.tkpm.sms.application.dto.response.status.StatusDto;
import com.tkpm.sms.domain.model.Status;

public interface StatusMapper {

    StatusDto toStatusDto(Status status);

    Status toStatus(StatusRequestDto statusRequestDto);

    void updateStatusFromDto(StatusRequestDto statusRequestDto, Status status);
}