package com.tkpm.sms.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enumeration of all error codes in the domain layer.
 * Each error code has a name that can be used to identify the error type.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // System Errors
    UNCATEGORIZED("Uncategorized Exception"),
    INVALID_ERROR_KEY("Invalid error key"),
    FAIL_TO_EXPORT_FILE("Failed to export file"),
    FAIL_TO_IMPORT_FILE("Failed to import file"),
    INTERNAL_SERVER_ERROR("An unexpected error occurred"),

    // Validation Errors
    INVALID_PHONE("Invalid phone number"),
    INVALID_PHONE_SETTING_DETAILS("Invalid phone setting details"),

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
    INVALID_COURSE("Invalid course"),

    // Resource Errors
    NOT_FOUND("Resource not found"),
    DUPLICATE_RESOURCE("Resource already existed"),
    UNSUPPORTED_EMAIL_DOMAIN("Unsupported email domain"),
    INVALID_FILE_FORMAT("Invalid file format"),

    // Conflict Errors
    UNSUPPORTED_STATUS_TRANSITION("Unsupported status transition");

    private final String defaultMessage;

    /**
     * Returns the name of the error code.
     * This is the enum name itself, which serves as the error code identifier.
     *
     * @return The error code name
     */
    public String getName() {
        return this.name();
    }
}