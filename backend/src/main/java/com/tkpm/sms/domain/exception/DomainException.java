package com.tkpm.sms.domain.exception;

import lombok.Getter;

@Getter
public class DomainException extends RuntimeException {
    public DomainException(String message) {
        super(message);
    }
}