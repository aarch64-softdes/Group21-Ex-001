package com.tkpm.sms.domain.exception;

public class UnenrollableCourseException extends DomainException {
    public UnenrollableCourseException(String message) {
        super(message, ErrorCode.UNENROLLABLE_COURSE);
    }
}
