package com.tkpm.sms.domain.exception;

public class FileProcessingException extends DomainException {
    public FileProcessingException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public FileProcessingException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }
}