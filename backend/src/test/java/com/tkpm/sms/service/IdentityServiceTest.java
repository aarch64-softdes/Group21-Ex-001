package com.tkpm.sms.service;

import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.application.mapper.IdentityMapper;
import com.tkpm.sms.application.service.implementation.IdentityServiceImpl;
import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.repository.IdentityRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import com.tkpm.sms.domain.service.validators.IdentityDomainValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
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

    @Mock
    private TranslatorService translatorService;

    @InjectMocks
    private IdentityServiceImpl identityService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup common mock behaviors
        when(translatorService.getEntityTranslatedName(Identity.class)).thenReturn("Identity");
    }

    @Test
    void testCreateIdentity() {
        // Setup
        String identityTypeStr = "Passport";
        String identityNumber = "123456789";

        IdentityCreateRequestDto requestDto = new IdentityCreateRequestDto();
        requestDto.setType(identityTypeStr);
        requestDto.setNumber(identityNumber);

        Identity identity = new Identity();
        Identity savedIdentity = new Identity();

        try (MockedStatic<IdentityType> mockedIdentityType = mockStatic(IdentityType.class)) {
            mockedIdentityType.when(() -> IdentityType.fromDisplayName(identityTypeStr))
                    .thenReturn(IdentityType.PASSPORT);

            when(identityMapper.toIdentity(requestDto)).thenReturn(identity);
            when(identityRepository.save(identity)).thenReturn(savedIdentity);
            doNothing().when(identityDomainValidator)
                    .validateIdentityUniqueness(IdentityType.PASSPORT, identityNumber);

            // Execute
            Identity createdIdentity = identityService.createIdentity(requestDto);

            // Verify
            assertNotNull(createdIdentity);
            assertEquals(savedIdentity, createdIdentity);

            verify(identityDomainValidator).validateIdentityUniqueness(IdentityType.PASSPORT,
                    identityNumber);
            verify(identityMapper).toIdentity(requestDto);
            verify(identityRepository).save(identity);
        }
    }

    @Test
    void testUpdateIdentity_NotFound() {
        // Setup
        String id = "1";
        String identityTypeStr = "Passport";
        String identityNumber = "123456789";

        IdentityUpdateRequestDto requestDto = new IdentityUpdateRequestDto();
        requestDto.setType(identityTypeStr);
        requestDto.setNumber(identityNumber);

        try (MockedStatic<IdentityType> mockedIdentityType = mockStatic(IdentityType.class)) {
            mockedIdentityType.when(() -> IdentityType.fromDisplayName(identityTypeStr))
                    .thenReturn(IdentityType.PASSPORT);

            when(identityRepository.findById(id)).thenReturn(Optional.empty());
            doNothing().when(identityDomainValidator)
                    .validateIdentityUniquenessForUpdate(IdentityType.PASSPORT, identityNumber, id);

            // Execute & Verify
            ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                    () -> identityService.updateIdentity(id, requestDto));

            verify(identityDomainValidator)
                    .validateIdentityUniquenessForUpdate(IdentityType.PASSPORT, identityNumber, id);
            verify(identityRepository).findById(id);
            verify(translatorService).getEntityTranslatedName(Identity.class);
            verifyNoInteractions(identityMapper);
            verifyNoMoreInteractions(identityRepository);
        }
    }

    @Test
    void testUpdateIdentity_Success() {
        // Setup
        String id = "1";
        String identityTypeStr = "Passport";
        String identityNumber = "123456789";

        IdentityUpdateRequestDto requestDto = new IdentityUpdateRequestDto();
        requestDto.setType(identityTypeStr);
        requestDto.setNumber(identityNumber);

        Identity existingIdentity = new Identity();
        Identity updatedIdentity = new Identity();

        try (MockedStatic<IdentityType> mockedIdentityType = mockStatic(IdentityType.class)) {
            mockedIdentityType.when(() -> IdentityType.fromDisplayName(identityTypeStr))
                    .thenReturn(IdentityType.PASSPORT);

            when(identityRepository.findById(id)).thenReturn(Optional.of(existingIdentity));
            doNothing().when(identityDomainValidator)
                    .validateIdentityUniquenessForUpdate(IdentityType.PASSPORT, identityNumber, id);
            doNothing().when(identityMapper).updateIdentityFromDto(requestDto, existingIdentity);
            when(identityRepository.save(existingIdentity)).thenReturn(updatedIdentity);

            // Execute
            Identity result = identityService.updateIdentity(id, requestDto);

            // Verify
            assertNotNull(result);
            assertEquals(updatedIdentity, result);

            verify(identityDomainValidator)
                    .validateIdentityUniquenessForUpdate(IdentityType.PASSPORT, identityNumber, id);
            verify(identityRepository).findById(id);
            verify(identityMapper).updateIdentityFromDto(requestDto, existingIdentity);
            verify(identityRepository).save(existingIdentity);
        }
    }
}