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
    NULL_VALUE("Value cannot be null", HttpStatus.BAD_REQUEST),

    NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    CONFLICT("Resource already existed", HttpStatus.CONFLICT),

    //Identity
    INVALID_IDENTITY_TYPE("Invalid identity type, available values are {values}", HttpStatus.BAD_REQUEST),
    INVALID_IDENTITY_NUMBER("Invalid identity number", HttpStatus.BAD_REQUEST),
    INVALID_IDENTITY_ISSUED_DATE("Identity issued date must be before expired date", HttpStatus.BAD_REQUEST),

    // File error
    FAIL_TO_EXPORT_FILE("Fail to export file", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_FORMAT("Invalid file format", HttpStatus.BAD_REQUEST),
    ;

    @Setter
    private String message;
    private HttpStatus httpStatus;

    public ErrorCode withMessage(String newMessage) {
        ErrorCode copy = this;
        copy.message = newMessage;
        return copy;
    }
}
