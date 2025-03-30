package com.tkpm.sms.application.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark methods where domain exceptions should be
 * translated to application exceptions.
 * 
 * When applied to a method, the DomainExceptionHandlerAspect will
 * intercept any DomainException thrown by the method and translate
 * it to an appropriate ApplicationException.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TranslateDomainException {
}