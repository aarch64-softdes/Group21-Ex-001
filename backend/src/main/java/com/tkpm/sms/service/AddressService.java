package com.tkpm.sms.service;

import com.tkpm.sms.dto.request.AddressCreateRequestDto;
import com.tkpm.sms.dto.request.AddressUpdateRequestDto;
import com.tkpm.sms.entity.Address;
import com.tkpm.sms.mapper.AddressMapper;
import com.tkpm.sms.repository.AddressRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressService {
    AddressMapper addressMapper;
    AddressRepository addressRepository;

    @Transactional
    public Address createAddress(AddressCreateRequestDto addressCreateRequestDto) {
        Address address = addressMapper.createAddress(addressCreateRequestDto);
        return addressRepository.save(address);
    }

    @Transactional
    public Address updateAddress(AddressUpdateRequestDto addressUpdateRequestDto, String id) {
        Address address = addressRepository.findById(id).orElseThrow();
        addressMapper.updateAddress(address, addressUpdateRequestDto);
        return addressRepository.save(address);
    }
}
