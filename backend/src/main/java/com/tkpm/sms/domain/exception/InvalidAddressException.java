package com.tkpm.sms.domain.exception;

public class InvalidAddressException extends DomainException {
    public InvalidAddressException(String message) {
        super(message);
    }
}