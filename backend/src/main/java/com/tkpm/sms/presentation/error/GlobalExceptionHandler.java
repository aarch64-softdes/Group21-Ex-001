package com.tkpm.sms.presentation.error;

import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.application.exception.ApplicationException;
import com.tkpm.sms.domain.exception.ErrorCode;
import jakarta.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@ControllerAdvice
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GlobalExceptionHandler {
    static String VALUES_ATTRIBUTE = "values";
    static String FIELD_ATTRIBUTE = "field";

    ErrorCodeStatusMapper errorCodeStatusMapper;

    @Autowired
    public GlobalExceptionHandler(ErrorCodeStatusMapper errorCodeStatusMapper) {
        this.errorCodeStatusMapper = errorCodeStatusMapper;
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleApplicationException(ApplicationException exception) {
        log.error("Application error", exception);

        ErrorCode errorCode = exception.getErrorCode();
        HttpStatus status = errorCodeStatusMapper.getStatus(errorCode);
        var response = ApplicationResponseDto.failure(errorCode, status.value());

        return ResponseEntity
                .status(errorCodeStatusMapper.getStatus(errorCode))
                .body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleRuntimeException(RuntimeException exception) {
        log.error("Unexpected error", exception);

        var errorCode = ErrorCode.UNCATEGORIZED;
        HttpStatus status = errorCodeStatusMapper.getStatus(errorCode);
        var response = ApplicationResponseDto.failure(errorCode, status.value());

        return ResponseEntity
                .status(status)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleValidationException(MethodArgumentNotValidException exception) {
        log.error("Validation error", exception);

        FieldError fieldError = exception.getFieldError();
        if (fieldError == null) {
            return handleInvalidErrorKey();
        }

        String enumKey = fieldError.getDefaultMessage();
        ErrorCode error = processValidationError(exception, enumKey);

        return ResponseEntity
                .badRequest()
                .body(ApplicationResponseDto.failure(error));
    }

    private ErrorCode processValidationError(
            MethodArgumentNotValidException exception,
            String enumKey
    ) {
        try {
            ConstraintViolation<?> constraintViolation = exception
                    .getBindingResult()
                    .getAllErrors()
                    .get(0)
                    .unwrap(ConstraintViolation.class);

            Map<String, Object> attributes =
                    constraintViolation.getConstraintDescriptor().getAttributes();

            ErrorCode error = ErrorCode.valueOf(enumKey);

            if (attributes.containsKey(FIELD_ATTRIBUTE)) {
                String requiredField = attributes.get(FIELD_ATTRIBUTE).toString();
                error.setMessage(formatRequiredMessage(requiredField));
            }

            if (attributes.containsKey(VALUES_ATTRIBUTE)) {
                error.setMessage(formatEnumValues(error.getMessage(), attributes));
            }

            return error;
        } catch (Exception e) {
            log.debug("Error processing validation error", e);
            return ErrorCode.INVALID_ERROR_KEY;
        }
    }

    private ResponseEntity<ApplicationResponseDto<Object>> handleInvalidErrorKey() {
        ErrorCode errorCode = ErrorCode.INVALID_ERROR_KEY;
        return ResponseEntity
                .badRequest()
                .body(ApplicationResponseDto.failure(errorCode));
    }

    private String formatEnumValues(String message, Map<String, Object> params) {
        String[] values = (String[]) params.get(VALUES_ATTRIBUTE);
        return message.replace("{" + "values" + "}", Arrays.toString(values));
    }

    private String formatRequiredMessage(String requiredField) {
        return String.format("%s is required", requiredField);
    }
}
