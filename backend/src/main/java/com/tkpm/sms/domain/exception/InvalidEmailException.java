package com.tkpm.sms.domain.exception;

public class InvalidEmailException extends DomainException {
    public InvalidEmailException(String message) {
        super(message, ErrorCode.INVALID_EMAIL);
    }
}