package com.tkpm.sms.domain.exception;

public class CourseExpiredException extends DomainException {
    public CourseExpiredException(String message) {
        super(message, ErrorCode.COURSE_EXPIRED);
    }
}
