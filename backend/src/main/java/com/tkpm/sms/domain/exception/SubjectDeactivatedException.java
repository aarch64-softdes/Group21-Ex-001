package com.tkpm.sms.domain.exception;

public class SubjectDeactivatedException extends DomainException {
    public SubjectDeactivatedException(String message) {
        super(message, ErrorCode.SUBJECT_DEACTIVATED);
    }
    public SubjectDeactivatedException(String messageKey, Object... args) {
        super(ErrorCode.SUBJECT_DEACTIVATED, messageKey, args);
    }
}
