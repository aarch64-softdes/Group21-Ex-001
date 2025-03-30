package com.tkpm.sms.presentation.error;

import com.tkpm.sms.application.exception.NewApplicationException;
import com.tkpm.sms.domain.exception.DomainException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final ErrorCodeStatusMapper errorCodeStatusMapper;
    private final com.tkpm.sms.application.exception.ExceptionTranslator exceptionTranslator;

    @ExceptionHandler(NewApplicationException.class)
    public ResponseEntity<ApiError> handleApplicationException(NewApplicationException ex) {
        log.error("Application exception occurred: {}", ex.getMessage(), ex);

        HttpStatus status = errorCodeStatusMapper.getStatus(ex.getErrorCode());
        ApiError apiError = ApiError.of(
                ex.getErrorCode().name(),
                ex.getMessage()
        );

        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApiError> handleDomainException(DomainException ex) {
        log.error("Domain exception occurred: {}", ex.getMessage(), ex);

        // Translate domain exception to application exception
        NewApplicationException applicationException = exceptionTranslator.translateException(ex);

        // Then handle it as an application exception
        return handleApplicationException(applicationException);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.error("Validation exception occurred: {}", ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        String errorMessage = errors.entrySet().stream()
                .map(entry -> entry.getKey() + ": " + entry.getValue())
                .collect(Collectors.joining(", "));

        ApiError apiError = ApiError.of("VALIDATION_ERROR", errorMessage);
        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGenericException(Exception ex) {
        log.error("Unexpected exception occurred: {}", ex.getMessage(), ex);

        ApiError apiError = ApiError.of("INTERNAL_SERVER_ERROR", "An unexpected error occurred");
        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}