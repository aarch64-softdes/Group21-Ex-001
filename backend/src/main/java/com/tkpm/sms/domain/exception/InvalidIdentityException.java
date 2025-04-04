package com.tkpm.sms.domain.exception;

public class InvalidIdentityException extends DomainException {
    public InvalidIdentityException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
