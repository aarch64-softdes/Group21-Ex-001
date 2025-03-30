package com.tkpm.sms.domain.exception;

public class InvalidIdentityException extends DomainException {
    public InvalidIdentityException(String message) {
        super(message, "INVALID_IDENTITY");
    }
}