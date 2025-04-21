package com.tkpm.sms.domain.exception;

public class FileProcessingException extends DomainException {
    public FileProcessingException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}