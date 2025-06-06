package com.tkpm.sms.presentation.error;

import com.tkpm.sms.domain.exception.ErrorCode;
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
        mappings.put(ErrorCode.INVALID_PHONE, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_PHONE_SETTING_DETAILS, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_IDENTITY_TYPE, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_IDENTITY_CARD_NUMBER, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_CHIP_BASE_NUMBER, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_PASSPORT_NUMBER, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_IDENTITY_ISSUED_DATE, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.FIELD_REQUIRED, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_FILE_FORMAT, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.UNENROLLABLE_COURSE, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_SUBJECT_CREDITS, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.STUDENT_PREREQUISITES_NOT_SATISFIED, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.INVALID_COURSE_SCHEDULE_TIME, HttpStatus.BAD_REQUEST);

        // Resource Errors (4xx)
        mappings.put(ErrorCode.NOT_FOUND, HttpStatus.NOT_FOUND);
        mappings.put(ErrorCode.DUPLICATE_RESOURCE, HttpStatus.CONFLICT);
        mappings.put(ErrorCode.UNSUPPORTED_EMAIL_DOMAIN, HttpStatus.CONFLICT);
        mappings.put(ErrorCode.UNSUPPORTED_STATUS_TRANSITION, HttpStatus.CONFLICT);

        // Subject Errors (4xx)
        mappings.put(ErrorCode.SUBJECT_DELETION_CONSTRAINT, HttpStatus.BAD_REQUEST);
        mappings.put(ErrorCode.SUBJECT_DEACTIVATED, HttpStatus.BAD_REQUEST);
    }

    public HttpStatus getStatus(ErrorCode errorCode) {
        return mappings.getOrDefault(errorCode, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}