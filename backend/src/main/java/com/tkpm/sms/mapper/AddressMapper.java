package com.tkpm.sms.mapper;

import com.tkpm.sms.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.dto.request.address.AddressUpdateRequestDto;
import com.tkpm.sms.entity.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address createAddress(AddressCreateRequestDto addressCreateRequestDto);
    
    void updateAddress(@MappingTarget Address address, AddressUpdateRequestDto addressCreateRequestDto);
    
    AddressCreateRequestDto updateToCreateRequest(AddressUpdateRequestDto requestDto);
}
