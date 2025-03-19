package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.AddressCreateRequestDto;
import com.tkpm.sms.dto.request.AddressUpdateRequestDto;
import com.tkpm.sms.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    Address createAddress(AddressCreateRequestDto addressCreateRequestDto);
    void updateAddress(@MappingTarget Address address, AddressUpdateRequestDto addressCreateRequestDto);

    AddressCreateRequestDto updateToCreateRequest(AddressUpdateRequestDto requestDto);
}
