package com.tkpm.sms.application.validation;

import com.tkpm.sms.application.annotation.IdentityConstraint;
import com.tkpm.sms.application.dto.request.identity.IdentityCreateRequestDto;
import com.tkpm.sms.application.dto.request.identity.IdentityUpdateRequestDto;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.enums.IdentityType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Slf4j
public class IdentityValidator implements ConstraintValidator<IdentityConstraint, Object> {

    private final Map<IdentityType, ErrorCode> identityTypeErrorCodeMap =
            Map.of(
                    IdentityType.CHIP_CARD, ErrorCode.INVALID_CHIP_BASE_NUMBER,
                    IdentityType.IDENTITY_CARD, ErrorCode.INVALID_IDENTITY_CARD_NUMBER,
                    IdentityType.PASSPORT, ErrorCode.INVALID_PASSPORT_NUMBER
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

        IdentityType identity = IdentityType.fromDisplayName(type);
        context.disableDefaultConstraintViolation();

        if (Objects.isNull(identity)) {
            addValidationError(
                    ErrorCode.INVALID_IDENTITY_TYPE,
                    "type", context);

            return false;
        }

        if (!identity.isValidNumber(number)) {
            addValidationError(
                    identityTypeErrorCodeMap.get(identity),
                    "number", context);

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

    private void addValidationError(ErrorCode errorCode, String propertyType, ConstraintValidatorContext context) {
        context.buildConstraintViolationWithTemplate(errorCode.name())
                .addPropertyNode(propertyType)
                .addConstraintViolation();
    }
}