package com.tkpm.sms.application.validation;

import com.tkpm.sms.application.annotation.RequiredConstraint;
import com.tkpm.sms.domain.exception.ErrorCode;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
public class RequiredValidator implements ConstraintValidator<RequiredConstraint, Object> {
    @Override
    public void initialize(RequiredConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        if (Objects.isNull(value)) {
            addConstraintViolation(context, context.getDefaultConstraintMessageTemplate());
            return false;
        }

        if (value instanceof String && ((String) value).isBlank()) {
            addConstraintViolation(context, context.getDefaultConstraintMessageTemplate());
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String fieldName) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(ErrorCode.FIELD_REQUIRED.name())
                .addPropertyNode(fieldName).addConstraintViolation();
    }
}