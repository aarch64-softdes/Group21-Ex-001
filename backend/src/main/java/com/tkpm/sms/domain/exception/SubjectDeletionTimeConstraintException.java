package com.tkpm.sms.domain.exception;

public class SubjectDeletionTimeConstraintException extends DomainException {
    public SubjectDeletionTimeConstraintException(String message) {
        super(message, ErrorCode.SUBJECT_DELETION_TIME_CONSTRAINT);
    }
}
