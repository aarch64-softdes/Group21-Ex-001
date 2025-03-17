package com.tkpm.sms.exceptions;

import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED("Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_ERROR_KEY("Invalid error key", HttpStatus.INTERNAL_SERVER_ERROR),

    //Validate
    INVALID_NAME("Invalid name", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL("Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_PHONE("Invalid phone number", HttpStatus.BAD_REQUEST),
    INVALID_STATUS("Invalid status", HttpStatus.BAD_REQUEST),

    NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    CONFLICT("Resource already existed", HttpStatus.CONFLICT),
    ;

    @Setter
    private String message;
    private HttpStatus httpStatus;
}
