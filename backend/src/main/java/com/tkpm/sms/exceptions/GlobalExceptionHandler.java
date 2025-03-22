package com.tkpm.sms.exceptions;

import com.tkpm.sms.dto.response.common.ApplicationResponseDto;
import jakarta.validation.ConstraintViolation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private final String VALUES_ATTRIBUTE = "values";

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleApplicationException(ApplicationException exception) {
        log.error("Application error", exception);

        ErrorCode errorCode = exception.getErrorCode();
        var response = ApplicationResponseDto.failure(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleRuntimeException(RuntimeException exception) {
        log.error("Unexpected error", exception);

        var errorCode = ErrorCode.UNCATEGORIZED;
        var response = ApplicationResponseDto.failure(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleValidationException(MethodArgumentNotValidException exception) {
        log.error("Validation error", exception);

        var defaultMessage = exception.getFieldError().getDefaultMessage();
        var error = ErrorCode.INVALID_ERROR_KEY;
        log.info("Default message: {}", defaultMessage);
        try {
            if (defaultMessage != null && defaultMessage.contains(";")) {
                var enumKey = defaultMessage.split(";")[0];
                var message = defaultMessage.split(";")[1];
                log.info("Enum key: {}", enumKey);
                error = ErrorCode.valueOf(enumKey);

                if (message != null && !message.isEmpty()) {
                    error.setMessage(message);
                }
            } else {
                error = ErrorCode.valueOf(defaultMessage);
            }

            var constrainViolation = exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
            var attributes = constrainViolation.getConstraintDescriptor().getAttributes();
            log.info("Attributes: {}", attributes);
            if (attributes.containsKey(VALUES_ATTRIBUTE)) {
                error.setMessage(mapEnumAttribute(error.getMessage(), attributes));
            }

        } catch (Exception e) {
            // Ignore
        }
        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.badRequest().body(response);
    }

    private String mapEnumAttribute(String message, Map<String, Object> params) {
        String[] values = (String[]) params.get(VALUES_ATTRIBUTE);

        return message.replace("{" + "values" + "}", Arrays.toString(values));
    }
}

