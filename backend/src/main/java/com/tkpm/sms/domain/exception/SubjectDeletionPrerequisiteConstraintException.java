package com.tkpm.sms.domain.exception;

public class SubjectDeletionPrerequisiteConstraintException extends DomainException {
    public SubjectDeletionPrerequisiteConstraintException(String message) {
        super(message, ErrorCode.SUBJECT_DELETION_PREREQUISITE_CONSTRAINT);
    }
}
