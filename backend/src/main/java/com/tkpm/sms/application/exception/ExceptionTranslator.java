package com.tkpm.sms.application.exception;

import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class ExceptionTranslator {
    public NewApplicationException translateException(DomainException exception) {
        if (exception instanceof ResourceNotFoundException) {
            return new NewApplicationException(
                    ErrorCode.NOT_FOUND.withMessage(exception.getMessage())
            );
        } else if (exception instanceof DuplicateResourceException) {
            return new NewApplicationException(
                    ErrorCode.CONFLICT.withMessage(exception.getMessage())
            );
        }

        // Default translation
        return new NewApplicationException(
                ErrorCode.UNCATEGORIZED.withMessage(exception.getMessage())
        );
    }
}