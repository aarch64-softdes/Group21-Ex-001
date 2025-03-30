package com.tkpm.sms.application.validator.status;

import com.tkpm.sms.enums.Status;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Objects;

public class StatusValidator implements ConstraintValidator<StatusConstraint, String> {

    @Override
    public void initialize(StatusConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) {
            // Leave for @NotNull
            return true;
        }

        try {
            Status.valueOf(value);
        } catch (IllegalArgumentException e) {
            return false;
        }

        return true;
    }
}