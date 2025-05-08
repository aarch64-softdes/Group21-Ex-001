package com.tkpm.sms.domain.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }

    public ResourceNotFoundException(String messageKey, Object... args) {
        super(ErrorCode.NOT_FOUND, messageKey, args);
    }
}