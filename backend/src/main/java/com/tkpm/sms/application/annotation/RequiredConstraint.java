package com.tkpm.sms.application.annotation;

import com.tkpm.sms.application.validation.RequiredValidator;
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
