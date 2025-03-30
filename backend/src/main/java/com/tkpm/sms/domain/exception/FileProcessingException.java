package com.tkpm.sms.domain.exception;

public class FileProcessingException extends DomainException {
    public FileProcessingException(String message) {
        super(message, "FILE_PROCESSING_ERROR");
    }
}