package com.tkpm.sms.domain.exception;

public class UnsupportedStatusTransitionException extends DomainException {
    public UnsupportedStatusTransitionException(String message) {
        super(message, ErrorCode.UNSUPPORTED_STATUS_TRANSITION);
    }

    public UnsupportedStatusTransitionException(String message, Object... args) {
        super(ErrorCode.UNSUPPORTED_STATUS_TRANSITION, message, args);
    }
}