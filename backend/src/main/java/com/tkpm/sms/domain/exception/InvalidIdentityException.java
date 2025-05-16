package com.tkpm.sms.domain.exception;

public class InvalidIdentityException extends DomainException {
    public InvalidIdentityException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public InvalidIdentityException(ErrorCode errorCode, String messageKey, Object... args) {
        super(errorCode, messageKey, args);
    }
}
