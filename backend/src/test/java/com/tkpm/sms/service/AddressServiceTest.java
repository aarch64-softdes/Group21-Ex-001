package com.tkpm.sms.service;

import com.tkpm.sms.application.dto.request.address.AddressCreateRequestDto;
import com.tkpm.sms.application.dto.request.address.AddressUpdateRequestDto;
import com.tkpm.sms.application.mapper.AddressMapper;
import com.tkpm.sms.application.service.implementation.AddressServiceImpl;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Address;
import com.tkpm.sms.domain.repository.AddressRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @Mock
    private AddressMapper addressMapper;

    @Mock
    private DomainEntityNameTranslator domainEntityNameTranslator;

    @InjectMocks
    private AddressServiceImpl addressService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(domainEntityNameTranslator.getEntityTranslatedName(Address.class))
                .thenReturn("Address");
    }

    @Test
    void testCreateAddress() {
        AddressCreateRequestDto requestDto = new AddressCreateRequestDto();
        Address address = new Address();

        when(addressMapper.toAddress(requestDto)).thenReturn(address);
        when(addressRepository.save(address)).thenReturn(address);

        Address result = addressService.createAddress(requestDto);

        assertNotNull(result);
        verify(addressMapper).toAddress(requestDto);
        verify(addressRepository).save(address);
    }

    @Test
    void testUpdateAddress() {
        String id = "1";
        AddressUpdateRequestDto requestDto = new AddressUpdateRequestDto();
        Address address = new Address();

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));
        doNothing().when(addressMapper).updateAddressFromDto(requestDto, address);
        when(addressRepository.save(address)).thenReturn(address);

        Address result = addressService.updateAddress(id, requestDto);

        assertNotNull(result);
        verify(addressRepository).findById(id);
        verify(addressMapper).updateAddressFromDto(requestDto, address);
        verify(addressRepository).save(address);
    }

    @Test
    void testUpdateAddressNotFound() {
        String id = "1";
        AddressUpdateRequestDto requestDto = new AddressUpdateRequestDto();

        when(addressRepository.findById(id)).thenReturn(Optional.empty());
        when(domainEntityNameTranslator.getEntityTranslatedName(Address.class))
                .thenReturn("Address");

        assertThrows(ResourceNotFoundException.class,
                () -> addressService.updateAddress(id, requestDto));
        verify(addressRepository).findById("1");
        verify(domainEntityNameTranslator).getEntityTranslatedName(Address.class);
        verifyNoMoreInteractions(addressMapper, addressRepository);
    }

    @Test
    void testGetAddressById() {
        String id = "1";
        Address address = new Address();

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));

        Address result = addressService.getAddressById(id);

        assertNotNull(result);
        verify(addressRepository).findById(id);
    }

    @Test
    void testGetAddressByIdNotFound() {
        String id = "1";

        when(addressRepository.findById(id)).thenReturn(Optional.empty());
        when(domainEntityNameTranslator.getEntityTranslatedName(Address.class))
                .thenReturn("Address");

        assertThrows(ResourceNotFoundException.class, () -> addressService.getAddressById(id));
        verify(addressRepository).findById(id);
        verify(domainEntityNameTranslator).getEntityTranslatedName(Address.class);
    }
}