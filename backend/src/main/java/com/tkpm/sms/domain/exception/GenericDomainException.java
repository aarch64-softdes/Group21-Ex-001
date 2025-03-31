package com.tkpm.sms.domain.exception;

public class GenericDomainException extends DomainException {

    public GenericDomainException(String message, ErrorCode code) {
        super(message, code);
    }

    public GenericDomainException(String message, ErrorCode code, Throwable cause) {
        super(message, code, cause);
    }
}