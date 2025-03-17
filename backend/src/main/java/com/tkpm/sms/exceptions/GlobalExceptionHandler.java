package com.tkpm.sms.exceptions;

import com.tkpm.sms.dto.response.common.ApplicationResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
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
        var error = ErrorCode.valueOf(enumKey);

        var response = ApplicationResponseDto.failure(error);
        return ResponseEntity.badRequest().body(response);
    }
}

