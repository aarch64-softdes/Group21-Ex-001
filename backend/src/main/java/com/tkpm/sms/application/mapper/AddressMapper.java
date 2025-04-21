package com.tkpm.sms.application.mapper;

import com.tkpm.sms.application.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.application.dto.request.address.AddressUpdateRequestDto;
import com.tkpm.sms.application.dto.response.AddressDto;
import com.tkpm.sms.domain.model.Address;

public interface AddressMapper {

    Address toAddress(AddressCreateRequestDto requestDto);

    AddressDto toAddressDto(Address address);

    void updateAddressFromDto(AddressUpdateRequestDto requestDto, Address address);

    AddressCreateRequestDto updateToCreateRequest(AddressUpdateRequestDto requestDto);
}