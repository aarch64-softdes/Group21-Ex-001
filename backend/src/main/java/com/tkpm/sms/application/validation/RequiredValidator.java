package com.tkpm.sms.application.validation;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import com.tkpm.sms.domain.exception.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class RequiredValidator implements ConstraintValidator<RequiredConstraint, Object> {
    @Override
    public void initialize(RequiredConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            return false;
        }

        if (value instanceof String && ((String) value).isBlank()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.FIELD_REQUIRED.name())
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}