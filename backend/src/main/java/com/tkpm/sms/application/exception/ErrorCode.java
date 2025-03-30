package com.tkpm.sms.application.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // System Errors (5xx)
    UNCATEGORIZED("Uncategorized Exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_ERROR_KEY("Invalid error key", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_TO_EXPORT_FILE("Fail to export file", HttpStatus.INTERNAL_SERVER_ERROR),
    FAIL_TO_IMPORT_FILE("Failed to import file", HttpStatus.INTERNAL_SERVER_ERROR),
    
    // Validation Errors (400)
    INVALID_NAME("Invalid name, this field should only contain letter", HttpStatus.BAD_REQUEST),
    INVALID_EMAIL("Invalid email format", HttpStatus.BAD_REQUEST),
    INVALID_PHONE("Invalid phone number", HttpStatus.BAD_REQUEST),
    INVALID_STATUS("Invalid status", HttpStatus.BAD_REQUEST),
    INVALID_SETTING_NAME("Invalid setting name", HttpStatus.BAD_REQUEST),
    INVALID_PHONE_SETTING_DETAILS("Invalid phone setting details", HttpStatus.BAD_REQUEST),
    INVALID_SETTING_DETAILS("Invalid setting details", HttpStatus.BAD_REQUEST),

    // Identity Validation Errors (400)
    INVALID_IDENTITY_TYPE(
            "Invalid identity type, available values are {values}", HttpStatus.BAD_REQUEST),
    INVALID_IDENTITY_CARD_NUMBER(
            "Invalid identity number, with Identity Card the number should contain 9 digits",
            HttpStatus.BAD_REQUEST),
    INVALID_CHIP_BASE_NUMBER(
            "Invalid identity number, with Chip Base the number should contain 12 digits",
            HttpStatus.BAD_REQUEST),
    INVALID_PASSPORT_NUMBER(
            "Invalid identity number, with Passport the number should contain first 2 uppercase letter and 7 digits",
            HttpStatus.BAD_REQUEST),
    INVALID_IDENTITY_ISSUED_DATE(
            "Identity issued date must be before expired date", HttpStatus.BAD_REQUEST),
    FIELD_REQUIRED("{field} must be filled in", HttpStatus.BAD_REQUEST),

    // Resource Errors (4xx)
    NOT_FOUND("Resource not found", HttpStatus.NOT_FOUND),
    CONFLICT("Resource already existed", HttpStatus.CONFLICT),
    UNSUPPORTED_EMAIL_DOMAIN("Unsupported email domain", HttpStatus.BAD_REQUEST),
    INVALID_FILE_FORMAT("Invalid file format", HttpStatus.BAD_REQUEST),

    // Conflict Errors (409)
    UNSUPPORTED_STATUS_TRANSITION("Unsupported status transition", HttpStatus.CONFLICT),
    ;

    @Setter
    @NonFinal
    String message;
    HttpStatus httpStatus;

    public ErrorCode withMessage(String newMessage) {
        this.message = newMessage;
        return this;
    }
}
