package com.tkpm.sms.validator;

import com.tkpm.sms.enums.Status;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {StatusValidator.class})
public @interface StatusConstraint {
    String message() default "";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
