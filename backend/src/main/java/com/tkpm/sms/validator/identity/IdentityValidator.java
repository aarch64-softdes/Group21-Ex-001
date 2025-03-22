package com.tkpm.sms.validator.identity;

import com.tkpm.sms.dto.request.IdentityCreateRequestDto;
import com.tkpm.sms.dto.request.IdentityUpdateRequestDto;
import com.tkpm.sms.enums.IdentityType;
import com.tkpm.sms.exceptions.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class IdentityValidator implements ConstraintValidator<IdentityConstraint, Object> {
    private final Map<IdentityType, ErrorCode> identityTypeErrorCodeMap = Map.of(
            IdentityType.Chip_Card, ErrorCode.INVALID_CHIP_BASE_NUMBER,
            IdentityType.Identity_Card, ErrorCode.INVALID_IDENTITY_CARD_NUMBER,
            IdentityType.Passport, ErrorCode.INVALID_PASSPORT_NUMBER
    );

    @Override
    public void initialize(IdentityConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            // Leave for @NotNull
            return true;
        }

        String type;
        String number;
        LocalDate issueDate;
        LocalDate expiryDate;

        if (value instanceof IdentityCreateRequestDto identity) {
            type = identity.getType();
            number = identity.getNumber();
            issueDate = identity.getIssuedDate();
            expiryDate = identity.getExpiryDate();
        } else if (value instanceof IdentityUpdateRequestDto identity) {
            type = identity.getType();
            number = identity.getNumber();
            issueDate = identity.getIssuedDate();
            expiryDate = identity.getExpiryDate();
        } else {
            return false;
        }

        if (Strings.isEmpty(type) || Strings.isEmpty(number)
                || Objects.isNull(issueDate) || Objects.isNull(expiryDate)) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        var identity = IdentityType.fromString(type);
        log.info("Identity type: {}", identity);
        if (identity == null) {
            addValidationError(ErrorCode.INVALID_IDENTITY_TYPE, context);
            return false;
        }
        log.info("Contain key: {}", identityTypeErrorCodeMap.containsKey(identity));
        if (!number.matches(identity.getPattern())) {
            addValidationError(identityTypeErrorCodeMap.get(identity), context);
            return false;
        }

        if (issueDate.isAfter(expiryDate)) {
            context.buildConstraintViolationWithTemplate(ErrorCode.INVALID_IDENTITY_ISSUED_DATE.name())
                    .addPropertyNode("issuedDate")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }

    private void addValidationError(ErrorCode errorCode, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(errorCode.name())
                .addPropertyNode("type")
                .addConstraintViolation();
    }
}