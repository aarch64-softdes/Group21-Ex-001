package com.tkpm.sms.domain.exception;

public class InvalidPhoneNumberException extends DomainException {
    public InvalidPhoneNumberException(String message) {
        super(message, ErrorCode.INVALID_PHONE);
    }

    public InvalidPhoneNumberException(String message, Throwable cause) {
        super(message, ErrorCode.INVALID_PHONE, cause);
    }

    public InvalidPhoneNumberException(String messageKey, Object... args) {
        super(ErrorCode.INVALID_PHONE, messageKey, args);
    }
}