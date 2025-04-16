package com.tkpm.sms.domain.exception;

public class InvalidCourseException extends DomainException {
    public InvalidCourseException(String message) {
        super(message, ErrorCode.INVALID_COURSE);
    }
}
