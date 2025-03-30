package com.tkpm.sms.application.exception;

import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.exception.DuplicateResourceException;
import com.tkpm.sms.domain.exception.ResourceNotFoundException;
import com.tkpm.sms.domain.exception.InvalidStatusTransitionException;
import com.tkpm.sms.domain.exception.InvalidAddressException;
import com.tkpm.sms.domain.exception.InvalidIdentityException;
import com.tkpm.sms.domain.exception.InvalidPhoneNumberException;
import com.tkpm.sms.domain.exception.InvalidStudentException;
import com.tkpm.sms.domain.exception.FileProcessingException;
import org.springframework.stereotype.Component;

@Component
public class ExceptionTranslator {
    public ApplicationException translateException(DomainException exception) {
        if (exception instanceof ResourceNotFoundException) {
            return new ApplicationException(
                    ErrorCode.NOT_FOUND.withMessage(exception.getMessage())
            );
        } else if (exception instanceof DuplicateResourceException) {
            return new ApplicationException(
                    ErrorCode.CONFLICT.withMessage(exception.getMessage())
            );
        } else if (exception instanceof InvalidStatusTransitionException) {
            return new ApplicationException(
                    ErrorCode.UNSUPPORTED_STATUS_TRANSITION.withMessage(exception.getMessage())
            );
        } else if (exception instanceof InvalidAddressException) {
            return new ApplicationException(
                    ErrorCode.INVALID_ADDRESS.withMessage(exception.getMessage())
            );
        } else if (exception instanceof InvalidIdentityException) {
            return new ApplicationException(
                    ErrorCode.INVALID_IDENTITY_TYPE.withMessage(exception.getMessage())
            );
        } else if (exception instanceof InvalidPhoneNumberException) {
            return new ApplicationException(
                    ErrorCode.INVALID_PHONE.withMessage(exception.getMessage())
            );
        } else if (exception instanceof InvalidStudentException) {
            return new ApplicationException(
                    ErrorCode.VALIDATION_ERROR.withMessage(exception.getMessage())
            );
        } else if (exception instanceof FileProcessingException) {
            return new ApplicationException(
                    ErrorCode.FAIL_TO_IMPORT_FILE.withMessage(exception.getMessage())
            );
        }

        // Default translation
        return new ApplicationException(
                ErrorCode.UNCATEGORIZED.withMessage(exception.getMessage())
        );
    }
}