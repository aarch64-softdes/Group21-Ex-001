package com.tkpm.sms.domain.exception;

public class InvalidPhoneNumberException extends DomainException {
    public InvalidPhoneNumberException(String message) {
        super(message, "INVALID_PHONE_NUMBER");
    }
}