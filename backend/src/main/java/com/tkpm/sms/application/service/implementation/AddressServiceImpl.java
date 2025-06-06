package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.application.dto.request.address.AddressUpdateRequestDto;
import com.tkpm.sms.application.mapper.AddressMapper;
import com.tkpm.sms.application.service.interfaces.AddressService;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Address;
import com.tkpm.sms.domain.repository.AddressRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AddressServiceImpl implements AddressService {
    AddressRepository addressRepository;
    AddressMapper addressMapper;
    TranslatorService translatorService;

    @Override
    @Transactional
    public Address createAddress(AddressCreateRequestDto requestDto) {
        // Convert DTO to domain entity
        Address address = addressMapper.toAddress(requestDto);

        // Save and return
        return addressRepository.save(address);
    }

    @Override
    @Transactional
    public Address updateAddress(String id, AddressUpdateRequestDto requestDto) {
        // Find existing address
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Address.class), id));

        // Update the address
        addressMapper.updateAddressFromDto(requestDto, address);

        // Save and return
        return addressRepository.save(address);
    }

    @Override
    public Address getAddressById(String id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Address.class), id));
    }
}