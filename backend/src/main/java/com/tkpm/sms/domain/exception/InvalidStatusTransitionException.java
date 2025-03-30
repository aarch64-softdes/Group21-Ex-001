package com.tkpm.sms.domain.exception;

public class InvalidStatusTransitionException extends DomainException {
    public InvalidStatusTransitionException(String message) {
        super(message, "INVALID_STATUS_TRANSITION");
    }
}