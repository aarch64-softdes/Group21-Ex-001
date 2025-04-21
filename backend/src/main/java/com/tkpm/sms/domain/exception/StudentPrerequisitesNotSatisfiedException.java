package com.tkpm.sms.domain.exception;

public class StudentPrerequisitesNotSatisfiedException extends DomainException {
    public StudentPrerequisitesNotSatisfiedException(String message) {
        super(message, ErrorCode.STUDENT_PREREQUISITES_NOT_SATISFIED);
    }

    public StudentPrerequisitesNotSatisfiedException(String message, Throwable cause) {
        super(message, ErrorCode.STUDENT_PREREQUISITES_NOT_SATISFIED, cause);
    }
}