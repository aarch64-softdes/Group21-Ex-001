package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.application.dto.response.identity.IdentityDto;
import com.tkpm.sms.domain.model.Identity;

public interface IdentityMapper {

    Identity toIdentity(IdentityCreateRequestDto requestDto);

    Identity toIdentity(IdentityUpdateRequestDto requestDto);

    IdentityDto toIdentityDto(Identity identity);

    void updateIdentityFromDto(IdentityUpdateRequestDto requestDto, Identity identity);

    IdentityUpdateRequestDto toUpdateDto(IdentityCreateRequestDto requestDto);

    IdentityCreateRequestDto toCreateDto(IdentityUpdateRequestDto requestDto);
}