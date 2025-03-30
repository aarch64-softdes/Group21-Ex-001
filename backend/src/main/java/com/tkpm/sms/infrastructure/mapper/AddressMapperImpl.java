package com.tkpm.sms.infrastructure.mapper;

import com.tkpm.sms.application.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.application.dto.request.address.AddressUpdateRequestDto;
import com.tkpm.sms.application.dto.response.AddressDto;
import com.tkpm.sms.application.mapper.AddressMapper;
import com.tkpm.sms.domain.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AddressMapperImpl extends AddressMapper {

    @Mapping(target = "id", ignore = true)
    Address toAddress(AddressCreateRequestDto requestDto);

    @Mapping(target = "fullAddress", expression = "java(address.getFullAddress())")
    AddressDto toAddressDto(Address address);

    void updateAddressFromDto(AddressUpdateRequestDto requestDto, @MappingTarget Address address);

    AddressCreateRequestDto updateToCreateRequest(AddressUpdateRequestDto requestDto);
}