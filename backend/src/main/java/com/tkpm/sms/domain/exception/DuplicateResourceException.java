package com.tkpm.sms.domain.exception;

public class DuplicateResourceException extends DomainException {
    public DuplicateResourceException(String message) {
        super(message, ErrorCode.DUPLICATE_RESOURCE);
    }
}