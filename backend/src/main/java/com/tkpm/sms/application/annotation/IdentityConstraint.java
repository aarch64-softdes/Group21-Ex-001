package com.tkpm.sms.application.annotation;

import com.tkpm.sms.application.validation.IdentityValidator;
import jakarta.validation.Constraint;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {IdentityValidator.class})
public @interface IdentityConstraint {
    String[] values() default {};

    String message() default "";

    Class<?>[] groups() default {};

    Class<?>[] payload() default {};
}
