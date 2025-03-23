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
    private final String FIELD_ATTRIBUTE = "field";

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
        var enumKey = exception.getFieldError().getDefaultMessage();
        var error = ErrorCode.INVALID_ERROR_KEY;
        try {
            var constrainViolation = exception.getBindingResult().getAllErrors().get(0).unwrap(ConstraintViolation.class);
            var attributes = constrainViolation.getConstraintDescriptor().getAttributes();

            String requiredMessage = getRequiredMessage(attributes.get(FIELD_ATTRIBUTE).toString());
            error = ErrorCode.valueOf(enumKey);
            error.setMessage(requiredMessage);

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

    private String getRequiredMessage(String requiredField) {
        return String.format("%s is required", requiredField);
    }
}

