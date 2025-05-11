package com.tkpm.sms.domain.exception;

public class SubjectDeletionConstraintException extends DomainException {
    public SubjectDeletionConstraintException(String message) {
        super(message, ErrorCode.SUBJECT_DELETION_CONSTRAINT);
    }

    public SubjectDeletionConstraintException(String messageKey, Object... args) {
        super(ErrorCode.SUBJECT_DELETION_CONSTRAINT, messageKey, args);
    }
}
