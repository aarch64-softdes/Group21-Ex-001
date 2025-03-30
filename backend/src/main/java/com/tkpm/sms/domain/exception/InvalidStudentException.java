package com.tkpm.sms.domain.exception;

public class InvalidStudentException extends DomainException {
    public InvalidStudentException(String message) {
        super(message, "INVALID_STUDENT");
    }
}