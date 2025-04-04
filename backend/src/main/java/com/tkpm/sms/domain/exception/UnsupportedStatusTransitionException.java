package com.tkpm.sms.domain.exception;

public class UnsupportedStatusTransitionException extends DomainException {
    public UnsupportedStatusTransitionException(String message) {
        super(message, ErrorCode.UNSUPPORTED_STATUS_TRANSITION);
    }
}