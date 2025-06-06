package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.application.mapper.IdentityMapper;
import com.tkpm.sms.application.service.interfaces.IdentityService;
import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.repository.IdentityRepository;
import com.tkpm.sms.domain.service.TranslatorService;
import com.tkpm.sms.domain.service.validators.IdentityDomainValidator;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class IdentityServiceImpl implements IdentityService {
    IdentityRepository identityRepository;
    IdentityDomainValidator identityDomainValidator;
    IdentityMapper identityMapper;
    TranslatorService translatorService;

    @Override
    @Transactional
    public Identity createIdentity(IdentityCreateRequestDto requestDto) {
        // Convert string type to enum
        IdentityType identityType = IdentityType.fromDisplayName(requestDto.getType());

        // Validate uniqueness
        identityDomainValidator.validateIdentityUniqueness(identityType, requestDto.getNumber());

        // Convert DTO to domain entity and save
        Identity identity = identityMapper.toIdentity(requestDto);
        return identityRepository.save(identity);
    }

    @Override
    @Transactional
    public Identity updateIdentity(String id, IdentityUpdateRequestDto requestDto) {
        // Convert string type to enum
        IdentityType identityType = IdentityType.fromDisplayName(requestDto.getType());

        // Validate uniqueness for update
        identityDomainValidator.validateIdentityUniquenessForUpdate(identityType,
                requestDto.getNumber(), id);

        // Find existing identity
        Identity identity = identityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.not_found.id",
                        translatorService.getEntityTranslatedName(Identity.class), id));

        // Update domain entity and save
        identityMapper.updateIdentityFromDto(requestDto, identity);
        return identityRepository.save(identity);
    }
}