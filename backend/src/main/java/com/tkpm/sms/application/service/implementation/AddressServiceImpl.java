package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.application.dto.request.address.AddressUpdateRequestDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.application.exception.ErrorCode;
import com.tkpm.sms.application.exception.ExceptionTranslator;
import com.tkpm.sms.application.mapper.AddressMapper;
import com.tkpm.sms.application.service.interfaces.AddressService;
import com.tkpm.sms.domain.model.Address;
import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.repository.AddressRepository;
import com.tkpm.sms.domain.service.validators.AddressDomainValidator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AddressServiceImpl implements AddressService {
    AddressRepository addressRepository;
    AddressDomainValidator addressDomainValidator;
    AddressMapper addressMapper;
    ExceptionTranslator exceptionTranslator;

    @Override
    @Transactional
    public Address createAddress(AddressCreateRequestDto requestDto) {
        try {
            // Convert DTO to domain entity
            Address address = addressMapper.toAddress(requestDto);

            // Validate the address
            addressDomainValidator.validateAddressFields(address);
            addressDomainValidator.validateAddressConsistency(address);

            // Save and return
            return addressRepository.save(address);
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    @Override
    @Transactional
    public Address updateAddress(String id, AddressUpdateRequestDto requestDto) {
        try {
            // Find existing address
            Address address = addressRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Address with id %s not found", id)));

            // Update the address
            addressMapper.updateAddressFromDto(requestDto, address);

            // Validate the updated address
            addressDomainValidator.validateAddressFields(address);
            addressDomainValidator.validateAddressConsistency(address);

            // Save and return
            return addressRepository.save(address);
        } catch (DomainException e) {
            throw exceptionTranslator.translateException(e);
        }
    }

    @Override
    public Address getAddressById(String id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(
                        ErrorCode.NOT_FOUND.withMessage(
                                String.format("Address with id %s not found", id)
                        )));
    }
}