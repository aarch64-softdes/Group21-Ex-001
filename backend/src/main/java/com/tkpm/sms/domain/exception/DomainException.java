package com.tkpm.sms.domain.exception;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
    private final ErrorCode code;
    private final Object[] messageArgs;
    private final String messageKey;

    protected DomainException(String message, ErrorCode errorCode, Object[] messageArgs,
            String messageKey) {
        super(message);
        this.code = errorCode;
        this.messageKey = messageKey != null
                ? messageKey
                : "error." + errorCode.name().toLowerCase();
        this.messageArgs = messageArgs != null ? messageArgs : new Object[0];
    }

    protected DomainException(String message, ErrorCode errorCode, Throwable cause) {
        super(message, cause);
        this.code = errorCode;
        this.messageArgs = null;
        this.messageKey = null;
    }

    protected DomainException(ErrorCode errorCode, String messageKey, Object[] messageArgs) {
        super(errorCode.getDefaultMessage());
        this.code = errorCode;
        this.messageArgs = messageArgs;
        this.messageKey = messageKey;
    }

    protected DomainException(String message, ErrorCode code) {
        super(message);
        this.code = code;
        this.messageArgs = null;
        this.messageKey = null;
    }
}