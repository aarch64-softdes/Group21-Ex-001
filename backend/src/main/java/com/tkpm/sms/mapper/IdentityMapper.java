package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.IdentityCreateRequestDto;
import com.tkpm.sms.dto.request.IdentityUpdateRequestDto;
import com.tkpm.sms.dto.response.identity.IdentityDto;
import com.tkpm.sms.entity.Identity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface IdentityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    Identity createIdentity(IdentityCreateRequestDto identityCreateRequestDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateIdentity(@MappingTarget Identity identity, IdentityUpdateRequestDto identityUpdateRequestDto);

    IdentityDto toIdentityDto(Identity identity);

    IdentityUpdateRequestDto toIdentityUpdateRequestDto(IdentityCreateRequestDto requestDto);
    IdentityCreateRequestDto toIdentityCreateRequestDto(IdentityUpdateRequestDto requestDto);
}
