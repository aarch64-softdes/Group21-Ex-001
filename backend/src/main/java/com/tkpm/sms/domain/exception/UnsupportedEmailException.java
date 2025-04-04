package com.tkpm.sms.domain.exception;

public class UnsupportedEmailException extends DomainException {
    public UnsupportedEmailException(String message) {
        super(message, ErrorCode.UNSUPPORTED_EMAIL_DOMAIN);
    }
}