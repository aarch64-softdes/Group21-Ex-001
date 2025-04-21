package com.tkpm.sms;

import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.application.mapper.IdentityMapper;
import com.tkpm.sms.application.service.implementation.IdentityServiceImpl;
import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.repository.IdentityRepository;
import com.tkpm.sms.domain.service.validators.IdentityDomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IdentityServiceTest {

    @Mock
    private IdentityRepository identityRepository;

    @Mock
    private IdentityDomainValidator identityDomainValidator;

    @Mock
    private IdentityMapper identityMapper;

    @InjectMocks
    private IdentityServiceImpl identityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateIdentity() {
        IdentityCreateRequestDto requestDto = new IdentityCreateRequestDto();
        requestDto.setType("Passport");
        requestDto.setNumber("123456789");

        IdentityType identityType = IdentityType.PASSPORT;
        Identity identity = new Identity();

        when(identityMapper.toIdentity(requestDto)).thenReturn(identity);
        when(identityRepository.save(identity)).thenReturn(identity);

        Identity createdIdentity = identityService.createIdentity(requestDto);

        assertNotNull(createdIdentity);
        verify(identityDomainValidator, times(1)).validateIdentityUniqueness(identityType,
                "123456789");
        verify(identityRepository, times(1)).save(identity);
    }

    @Test
    void testUpdateIdentity_NotFound() {
        String id = "1";
        IdentityUpdateRequestDto requestDto = new IdentityUpdateRequestDto();
        requestDto.setType("Passport");
        requestDto.setNumber("123456789");

        when(identityRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> identityService.updateIdentity(id, requestDto));
    }

    @Test
    void testUpdateIdentity_Success() {
        String id = "1";
        IdentityUpdateRequestDto requestDto = new IdentityUpdateRequestDto();
        requestDto.setType("Passport");
        requestDto.setNumber("123456789");

        IdentityType identityType = IdentityType.PASSPORT;
        Identity identity = new Identity();

        when(identityRepository.findById(id)).thenReturn(Optional.of(identity));
        doNothing().when(identityDomainValidator).validateIdentityUniquenessForUpdate(identityType,
                "123456789", id);
        doNothing().when(identityMapper).updateIdentityFromDto(requestDto, identity);
        when(identityRepository.save(identity)).thenReturn(identity);

        Identity updatedIdentity = identityService.updateIdentity(id, requestDto);

        assertNotNull(updatedIdentity);
        verify(identityDomainValidator, times(1)).validateIdentityUniquenessForUpdate(identityType,
                "123456789", id);
        verify(identityMapper, times(1)).updateIdentityFromDto(requestDto, identity);
        verify(identityRepository, times(1)).save(identity);
    }
}