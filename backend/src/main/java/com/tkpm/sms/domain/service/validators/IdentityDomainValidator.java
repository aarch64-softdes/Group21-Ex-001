package com.tkpm.sms.domain.service.validators;

import com.tkpm.sms.domain.enums.IdentityType;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.InvalidIdentityException;
import com.tkpm.sms.domain.repository.IdentityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class IdentityDomainValidator {
    private final IdentityRepository identityRepository;

    public void validateIdentityNumber(IdentityType type, String number) {
        if (!type.isValidNumber(number)) {
            String message = switch (type) {
                case IDENTITY_CARD -> "Invalid identity number, with Identity Card the number should contain 9 digits";
                case CHIP_CARD -> "Invalid identity number, with Chip Base the number should contain 12 digits";
                case PASSPORT -> "Invalid identity number, with Passport the number should contain first 2 uppercase letter and 7 digits";
            };

            throw new InvalidIdentityException(message);
        }
    }

    public void validateIssuedDateBeforeExpiryDate(LocalDate issuedDate, LocalDate expiryDate) {
        if (issuedDate.isAfter(expiryDate)) {
            throw new InvalidIdentityException("Identity issued date must be before expired date");
        }
    }

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