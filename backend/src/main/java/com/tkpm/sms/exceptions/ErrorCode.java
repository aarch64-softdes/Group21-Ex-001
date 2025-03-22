package com.tkpm.sms.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    UNCATEGORIZED("Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),

    INVALID_ERROR_KEY("Invalid error key", HttpStatus.INTERNAL_SERVER_ERROR),

    //Validate
    INVALID_NAME("Invalid name, this field should only contain letter", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL("Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_PHONE("Invalid phone number", HttpStatus.BAD_REQUEST),
    INVALID_STATUS("Invalid status", HttpStatus.BAD_REQUEST),
    NOT_NULL("Value cannot be null", HttpStatus.BAD_REQUEST),

    // Resource
    NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    CONFLICT("Resource already existed", HttpStatus.CONFLICT),

    // Identity
    INVALID_IDENTITY_TYPE("Invalid identity type, available values are {values}", HttpStatus.BAD_REQUEST),
    INVALID_IDENTITY_CARD_NUMBER("Invalid identity number, with Identity Card the number should contain 9 digits", HttpStatus.BAD_REQUEST),
    INVALID_CHIP_BASE_NUMBER("Invalid identity number, with Chip Base the number should contain 12 digits", HttpStatus.BAD_REQUEST),
    INVALID_PASSPORT_NUMBER("Invalid identity number, with Passport the number should contain first 2 uppercase letter and 7 digits", HttpStatus.BAD_REQUEST),
    INVALID_IDENTITY_ISSUED_DATE("Identity issued date must be before expired date", HttpStatus.BAD_REQUEST),

    // File error
    FAIL_TO_EXPORT_FILE("Fail to export file", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_FILE_FORMAT("Invalid file format", HttpStatus.BAD_REQUEST),
    FAIL_TO_IMPORT_FILE("Failed to import file", HttpStatus.INTERNAL_SERVER_ERROR),
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
