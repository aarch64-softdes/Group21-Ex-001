package com.tkpm.sms.presentation.error;

import com.tkpm.sms.application.exception.ErrorCode;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ErrorCodeStatusMapper {
    private final Map<ErrorCode, HttpStatus> mappings = new HashMap<>();

    public ErrorCodeStatusMapper() {
        // System Errors (5xx)
        mappings.put(ErrorCode.UNCATEGORIZED, HttpStatus.INTERNAL_SERVER_ERROR);
        mappings.put(ErrorCode.INVALID_ERROR_KEY, HttpStatus.INTERNAL_SERVER_ERROR);
        mappings.put(ErrorCode.FAIL_TO_EXPORT_FILE, HttpStatus.INTERNAL_SERVER_ERROR);
        mappings.put(ErrorCode.FAIL_TO_IMPORT_FILE, HttpStatus.INTERNAL_SERVER_ERROR);
        mappings.put(ErrorCode.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

        // Validation Errors (400)
        mappings.put(ErrorCode.VALIDATION_ERROR, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_NAME, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_EMAIL, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_PHONE, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_STATUS, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_SETTING_NAME, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_PHONE_SETTING_DETAILS, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_SETTING_DETAILS, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_ADDRESS, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_IDENTITY_TYPE, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_IDENTITY_CARD_NUMBER, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_CHIP_BASE_NUMBER, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_PASSPORT_NUMBER, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_IDENTITY_ISSUED_DATE, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.FIELD_REQUIRED, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.UNSUPPORTED_EMAIL_DOMAIN, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_FILE_FORMAT, HttpStatus.BAD_REQUEST);

        // Resource Errors (4xx)
        mappings.put(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
        mappings.put(ErrorCode.CONFLICT, HttpStatus.CONFLICT);
        mappings.put(ErrorCode.UNSUPPORTED_STATUS_TRANSITION, HttpStatus.CONFLICT);
    }

    public HttpStatus getStatus(ErrorCode errorCode) {
        return mappings.getOrDefault(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}