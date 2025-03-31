package com.tkpm.sms.domain.exception;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
    private final String code;

    protected DomainException(String message, String code) {
        super(message);
        this.code = code;
    }

    protected DomainException(String message, String code, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    protected DomainException(String message, ErrorCode errorCode) {
        super(message);
        this.code = errorCode.getName();
    }

    protected DomainException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.code = errorCode.getName();
    }

    protected DomainException(ErrorCode errorCode) {
        super(errorCode.getDefaultMessage());
        this.code = errorCode.getName();
    }
}