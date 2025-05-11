package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.model.Identity;
import com.tkpm.sms.domain.repository.IdentityRepository;
import com.tkpm.sms.domain.service.DomainEntityNameTranslator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IdentityDomainValidator {
    private final IdentityRepository identityRepository;
    private final DomainEntityNameTranslator domainEntityNameTranslator;

    public void validateIdentityUniqueness(IdentityType type, String number) {
        if (identityRepository.existsByNumberAndType(number, type)) {
            throw new DuplicateResourceException(
                    "error.identity.duplicate_resource.identity_number",
                    domainEntityNameTranslator.getEntityTranslatedName(Identity.class),
                    type.getDisplayName(), number);
        }
    }

    public void validateIdentityUniquenessForUpdate(IdentityType type, String number, String id) {
        if (identityRepository.existsByNumberAndTypeAndIdNot(number, type, id)) {
            throw new DuplicateResourceException(
                    "error.identity.duplicate_resource.identity_number",
                    domainEntityNameTranslator.getEntityTranslatedName(Identity.class),
                    type.getDisplayName(), number);
        }
    }
}