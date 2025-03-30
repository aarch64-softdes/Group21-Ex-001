package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.application.dto.response.identity.IdentityDto;
import com.tkpm.sms.application.mapper.IdentityMapper;
import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.enums.IdentityType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface IdentityMapperImpl extends IdentityMapper {

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToEnum")
    Identity toIdentity(IdentityCreateRequestDto requestDto);

    @Override
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "type", source = "type", qualifiedByName = "stringToEnum")
    Identity toIdentity(IdentityUpdateRequestDto requestDto);

    @Override
    @Mapping(target = "type", source = "type", qualifiedByName = "enumToString")
    IdentityDto toIdentityDto(Identity identity);

    @Override
    void updateIdentityFromDto(IdentityUpdateRequestDto requestDto, @MappingTarget Identity identity);

    @Override
    IdentityUpdateRequestDto toUpdateDto(IdentityCreateRequestDto requestDto);

    @Override
    IdentityCreateRequestDto toCreateDto(IdentityUpdateRequestDto requestDto);

    @Named("stringToEnum")
    default IdentityType stringToEnum(String type) {
        return type != null ? IdentityType.fromDisplayName(type) : null;
    }

    @Named("enumToString")
    default String enumToString(IdentityType type) {
        return type != null ? type.getDisplayName() : null;
    }
}