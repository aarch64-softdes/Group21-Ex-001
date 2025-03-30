package com.tkpm.sms.application.service.interfaces;

import com.tkpm.sms.application.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.application.dto.request.address.AddressUpdateRequestDto;
import com.tkpm.sms.domain.model.Address;

public interface AddressService {
    Address createAddress(AddressCreateRequestDto addressCreateRequestDto);
    Address updateAddress(String id, AddressUpdateRequestDto addressUpdateRequestDto);
    Address getAddressById(String id);
}