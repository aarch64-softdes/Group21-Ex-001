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
    UNCATEGORIZED("Uncategorized exception"),
    INTERNAL_SERVER_ERROR("An unexpected error occurred"),
    FILE_PROCESSING_ERROR("Error processing file"),

    // Resource Errors
    RESOURCE_NOT_FOUND("Resource not found"),
    RESOURCE_ALREADY_EXISTS("Resource already exists"),

    // Validation Errors
    VALIDATION_ERROR("Validation error occurred"),
    INVALID_NAME("Invalid name"),
    INVALID_EMAIL("Invalid email format"),
    INVALID_PHONE("Invalid phone number"),
    INVALID_ADDRESS("Invalid address"),
    INVALID_STATUS("Invalid status"),
    INVALID_FILE_FORMAT("Invalid file format"),
    FIELD_REQUIRED("Required field missing"),

    // Identity Validation Errors
    INVALID_IDENTITY_TYPE("Invalid identity type"),
    INVALID_IDENTITY_CARD_NUMBER("Invalid identity card number"),
    INVALID_CHIP_CARD_NUMBER("Invalid chip card number"),
    INVALID_PASSPORT_NUMBER("Invalid passport number"),
    INVALID_IDENTITY_ISSUED_DATE("Invalid identity issue date"),

    // Settings Errors
    INVALID_SETTING_NAME("Invalid setting name"),
    INVALID_SETTING_DETAILS("Invalid setting details"),
    INVALID_PHONE_SETTING_DETAILS("Invalid phone setting details"),
    UNSUPPORTED_EMAIL_DOMAIN("Unsupported email domain"),

    // Business Logic Errors
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