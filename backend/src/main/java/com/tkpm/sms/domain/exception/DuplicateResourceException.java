package com.tkpm.sms.domain.exception;

public class DuplicateResourceException extends DomainException {
    public DuplicateResourceException(String message) {
        super(message, "DUPLICATE_RESOURCE");
    }
}