package com.tkpm.sms.domain.exception;

public class ResourceNotFoundException extends DomainException {
    public ResourceNotFoundException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }
}