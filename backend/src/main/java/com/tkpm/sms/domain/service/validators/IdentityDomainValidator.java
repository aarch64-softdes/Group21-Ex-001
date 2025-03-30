package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.repository.IdentityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class IdentityDomainValidator {
    private final IdentityRepository identityRepository;

    public void validateIdentityUniqueness(IdentityType type, String number) {
        if (identityRepository.existsByNumberAndType(number, type)) {
            throw new DuplicateResourceException(
                    String.format("Identity with type %s and number %s already exists", type.getDisplayName(), number)
            );
        }
    }

    public void validateIdentityUniquenessForUpdate(IdentityType type, String number, String id) {
        if (identityRepository.existsByNumberAndTypeAndIdNot(number, type, id)) {
            throw new DuplicateResourceException(
                    String.format("Identity with type %s and number %s already exists", type.getDisplayName(), number)
            );
        }
    }
}