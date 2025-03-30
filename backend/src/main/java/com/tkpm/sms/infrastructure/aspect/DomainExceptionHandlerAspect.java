package com.tkpm.sms.infrastructure.aspect;

import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Aspect that intercepts methods annotated with @TranslateDomainException
 * and translates DomainException instances to application exceptions
 */
@Aspect
@Component
@Slf4j
public class DomainExceptionHandlerAspect {
    private static final Map<Class<? extends DomainException>, ErrorCode> ERROR_MAPPINGS = Map.of(
            ResourceNotFoundException.class, ErrorCode.NOT_FOUND,
            DuplicateResourceException.class, ErrorCode.CONFLICT,
            InvalidStatusTransitionException.class, ErrorCode.UNSUPPORTED_STATUS_TRANSITION,
            InvalidAddressException.class, ErrorCode.INVALID_ADDRESS,
            InvalidIdentityException.class, ErrorCode.INVALID_IDENTITY_TYPE,
            InvalidPhoneNumberException.class, ErrorCode.INVALID_PHONE,
            InvalidStudentException.class, ErrorCode.VALIDATION_ERROR,
            FileProcessingException.class, ErrorCode.FAIL_TO_IMPORT_FILE
    );

    @Around("@annotation(com.tkpm.sms.application.annotation.TranslateDomainException)")
    public Object handleDomainExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (DomainException e) {
            String methodName = joinPoint.getSignature().toShortString();
            log.debug("Translating domain exception in {}: {}", methodName, e.getMessage());
            throw translate(e);
        }
    }

    /**
     * Translates domain exceptions to application exceptions using error code mapping
     * while preserving the original exception message
     */
    private ApplicationException translate(DomainException exception) {
        ErrorCode errorCode = ERROR_MAPPINGS.getOrDefault(
                exception.getClass(),
                ErrorCode.UNCATEGORIZED
        );

        return new ApplicationException(errorCode.withMessage(exception.getMessage()));
    }
}