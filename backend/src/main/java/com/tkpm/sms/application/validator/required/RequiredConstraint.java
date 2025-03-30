package com.tkpm.sms.application.validator.required;

import jakarta.validation.Constraint;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {RequiredValidator.class})
@Documented
public @interface RequiredConstraint {
    String field() default "";

    String message() default "";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
