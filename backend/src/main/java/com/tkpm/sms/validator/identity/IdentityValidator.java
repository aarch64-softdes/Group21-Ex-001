package com.tkpm.sms.validator.identity;

import com.tkpm.sms.dto.request.IdentityCreateRequestDto;
import com.tkpm.sms.dto.request.IdentityUpdateRequestDto;
import com.tkpm.sms.enums.EnumUtils;
import com.tkpm.sms.enums.IdentityType;
import com.tkpm.sms.exceptions.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Slf4j
public class IdentityValidator implements ConstraintValidator<IdentityConstraint, Object> {
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
            log.error("Invalid object type: {}", value.getClass().getName());
            return false;
        }

        if (Objects.isNull(type) || Objects.isNull(number)
                || Objects.isNull(issueDate) || Objects.isNull(expiryDate)) {
            return true;
        }

        context.disableDefaultConstraintViolation();
        if (!isIdentityType(type)) {
            context.buildConstraintViolationWithTemplate(ErrorCode.INVALID_IDENTITY_TYPE.name())
                    .addPropertyNode("type")
                    .addConstraintViolation();
            log.info("Invalid identity type: {}", context.getDefaultConstraintMessageTemplate());
            return false;
        }
        if (!isValidNumber(type, number)) {
            context.buildConstraintViolationWithTemplate("INVALID_IDENTITY_NUMBER")
                    .addPropertyNode("number")
                    .addConstraintViolation();
            log.info("Invalid identity number: {}", context.getDefaultConstraintMessageTemplate());
            return false;
        }

        if (issueDate.isAfter(expiryDate)) {
            context.buildConstraintViolationWithTemplate("INVALID_IDENTITY_ISSUED_DATE")
                    .addPropertyNode("issuedDate")
                    .addConstraintViolation();
            log.info("Issue date must be before expiry date");
            return false;
        }

        return true;
    }

    private boolean isIdentityType(String value) {
        return Arrays.asList(EnumUtils.getNames(IdentityType.class)).contains(value);
    }

    private boolean isValidNumber(String type, String number) {
        if (IdentityType.ChipBased_Card.equals(type)) {
            return number.matches("[0-9]{12}$");
        } else if (IdentityType.Identity_Card.equals(type)) {
            return number.matches("[0-9]{9}$");
        } else if (IdentityType.Passport.equals(type)) {
            return number.matches("[A-Z]{2}[0-9]{7}$");
        }

        return false;
    }
}