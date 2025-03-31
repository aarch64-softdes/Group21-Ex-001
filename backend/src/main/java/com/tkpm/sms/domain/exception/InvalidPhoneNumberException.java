package com.tkpm.sms.domain.exception;

public class InvalidPhoneNumberException extends DomainException {
    public InvalidPhoneNumberException(String message) {
        super(message, ErrorCode.INVALID_PHONE);
    }
}