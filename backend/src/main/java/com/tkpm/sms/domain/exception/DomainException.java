package com.tkpm.sms.domain.exception;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
    private final ErrorCode code;

    protected DomainException(String message, ErrorCode errorCode) {
        super(message);
        this.code = errorCode;
    }

    protected DomainException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.code = errorCode;
    }

    protected DomainException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.code = errorCode;
    }
}