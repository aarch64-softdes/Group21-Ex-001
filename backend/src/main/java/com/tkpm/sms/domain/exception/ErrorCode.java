package com.tkpm.sms.domain.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ErrorCode {
    // System Errors
    UNCATEGORIZED("Uncategorized Exception"),
    INVALID_ERROR_KEY("Invalid error key"),
    FAIL_TO_EXPORT_FILE("Fail to export file"),
    FAIL_TO_IMPORT_FILE("Failed to import file"),
    INTERNAL_SERVER_ERROR("An unexpected error occurred"),

    // Validation Errors
    INVALID_NAME("Invalid name, this field should only contain letter"),
    INVALID_EMAIL("Invalid email format"),
    INVALID_PHONE("Invalid phone number"),
    INVALID_STATUS("Invalid status"),
    INVALID_SETTING_NAME("Invalid setting name"),
    INVALID_PHONE_SETTING_DETAILS("Invalid phone setting details"),
    INVALID_SETTING_DETAILS("Invalid setting details"),
    INVALID_ADDRESS("Invalid address"),
    VALIDATION_ERROR("Validation error occurred"),

    // Identity Validation Errors
    INVALID_IDENTITY_TYPE("Invalid identity type, available values are {values}"),
    INVALID_IDENTITY_CARD_NUMBER(
            "Invalid identity number, with Identity Card the number should contain 9 digits"),
    INVALID_CHIP_BASE_NUMBER(
            "Invalid identity number, with Chip Base the number should contain 12 digits"),
    INVALID_PASSPORT_NUMBER(
            "Invalid identity number, with Passport the number should contain first 2 uppercase letter and 7 digits"),
    INVALID_IDENTITY_ISSUED_DATE("Identity issued date must be before expired date"),
    FIELD_REQUIRED("{field} must be filled in"),

    // Resource Errors
    NOT_FOUND("Resource not found"),
    CONFLICT("Resource already existed"),
    UNSUPPORTED_EMAIL_DOMAIN("Unsupported email domain"),
    INVALID_FILE_FORMAT("Invalid file format"),

    // Conflict Errors
    UNSUPPORTED_STATUS_TRANSITION("Unsupported status transition");

    @Setter
    @NonFinal
    String message;

    public ErrorCode withMessage(String newMessage) {
        this.message = newMessage;
        return this;
    }
}