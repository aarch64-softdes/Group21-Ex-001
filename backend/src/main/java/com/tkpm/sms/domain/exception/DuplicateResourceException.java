package com.tkpm.sms.domain.exception;

public class DuplicateResourceException extends DomainException {
    public DuplicateResourceException(String message) {
        super(message, ErrorCode.DUPLICATE_RESOURCE);
    }

    public DuplicateResourceException(String messageKey, Object... args) {
        super(ErrorCode.DUPLICATE_RESOURCE, messageKey, args);
    }
}