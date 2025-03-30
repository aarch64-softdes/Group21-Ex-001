package com.tkpm.sms.application.service.implementation;

import com.tkpm.sms.application.annotation.TranslateDomainException;
import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.application.exception.ExceptionTranslator;
import com.tkpm.sms.application.mapper.IdentityMapper;
import com.tkpm.sms.application.service.interfaces.IdentityService;
import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.repository.IdentityRepository;
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
    ExceptionTranslator exceptionTranslator;

    @Override
    @Transactional
    @TranslateDomainException
    public Identity createIdentity(IdentityCreateRequestDto requestDto) {
        try {
            // Convert string type to enum
            IdentityType identityType = IdentityType.fromDisplayName(requestDto.getType());

            // Validate identity type and number
            identityDomainValidator.validateIdentityNumber(identityType, requestDto.getNumber());

            // Validate issued date and expiry date
            identityDomainValidator.validateIssuedDateBeforeExpiryDate(
                    requestDto.getIssuedDate(), requestDto.getExpiryDate());

            // Validate uniqueness
            identityDomainValidator.validateIdentityUniqueness(identityType, requestDto.getNumber());

            // Convert DTO to domain entity and save
            Identity identity = identityMapper.toIdentity(requestDto);
            return identityRepository.save(identity);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(
                    ErrorCode.INVALID_IDENTITY_TYPE.withMessage("Invalid identity type: " + requestDto.getType())
            );
        }
    }

    @Override
    @Transactional
    @TranslateDomainException
    public Identity updateIdentity(String id, IdentityUpdateRequestDto requestDto) {
        try{
            // Convert string type to enum
            IdentityType identityType = IdentityType.fromDisplayName(requestDto.getType());

            // Validate identity type and number
            identityDomainValidator.validateIdentityNumber(identityType, requestDto.getNumber());

            // Validate issued date and expiry date
            identityDomainValidator.validateIssuedDateBeforeExpiryDate(
                    requestDto.getIssuedDate(), requestDto.getExpiryDate());

            // Validate uniqueness for update
            identityDomainValidator.validateIdentityUniquenessForUpdate(identityType, requestDto.getNumber(), id);

            // Find existing identity
            Identity identity = identityRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            String.format("Identity with id %s not found", id)));

            // Update domain entity and save
            identityMapper.updateIdentityFromDto(requestDto, identity);
            return identityRepository.save(identity);
        } catch (IllegalArgumentException e) {
            throw new ApplicationException(
                    ErrorCode.INVALID_IDENTITY_TYPE.withMessage("Invalid identity type: " + requestDto.getType())
            );
        }
    }
}