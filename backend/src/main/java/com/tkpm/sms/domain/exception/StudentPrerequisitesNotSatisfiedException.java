package com.tkpm.sms.domain.exception;

public class StudentPrerequisitesNotSatisfiedException extends DomainException {
    public StudentPrerequisitesNotSatisfiedException(String message) {
        super(message, ErrorCode.STUDENT_PREREQUISITES_NOT_SATISFIED);
    }

    public StudentPrerequisitesNotSatisfiedException(String messageKey, Object... args) {
        super(ErrorCode.STUDENT_PREREQUISITES_NOT_SATISFIED, messageKey, args);
    }
}