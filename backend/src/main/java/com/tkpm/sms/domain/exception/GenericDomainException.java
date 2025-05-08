package com.tkpm.sms.domain.exception;

public class GenericDomainException extends DomainException {

    public GenericDomainException(String message, ErrorCode code) {
        super(message, code);
    }

    public GenericDomainException(String message, ErrorCode code, Throwable cause) {
        super(message, code, cause);
    }

    public GenericDomainException(String message) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR);
    }

    public GenericDomainException(String message, Throwable cause) {
        super(message, ErrorCode.INTERNAL_SERVER_ERROR, cause);
    }

    public GenericDomainException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}