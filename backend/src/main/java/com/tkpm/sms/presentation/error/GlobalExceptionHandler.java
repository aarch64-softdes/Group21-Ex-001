package com.tkpm.sms.presentation.error;

import com.tkpm.sms.application.dto.response.common.ApplicationResponseDto;
import com.tkpm.sms.domain.exception.DomainException;
import com.tkpm.sms.domain.exception.ErrorCode;
import com.tkpm.sms.domain.exception.GenericDomainException;
import jakarta.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
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
    static String MIN_VALUE_ATTRIBUTE = "value";
    static String PREFIX_ERROR = "error.";

    ErrorCodeStatusMapper errorCodeStatusMapper;
    MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(ErrorCodeStatusMapper errorCodeStatusMapper,
            MessageSource messageSource) {
        this.errorCodeStatusMapper = errorCodeStatusMapper;
        this.messageSource = messageSource;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleApplicationException(
            DomainException exception) {
        log.error("Application error", exception);

        ErrorCode errorCode = exception.getCode();
        HttpStatus status = errorCodeStatusMapper.getStatus(errorCode);
        String translatedMessage = exception.getMessage();
        if (exception.getMessageKey() != null) {
            translatedMessage = messageSource.getMessage(exception.getMessageKey(),
                    exception.getMessageArgs(), exception.getMessage(), // fallback
                    LocaleContextHolder.getLocale());
        }

        var response = ApplicationResponseDto
                .failure(new GenericDomainException(translatedMessage, errorCode), status.value());
        return ResponseEntity.status(errorCodeStatusMapper.getStatus(errorCode)).body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleRuntimeException(
            RuntimeException exception) {
        log.error("Unexpected error", exception);

        var errorCode = ErrorCode.UNCATEGORIZED;
        HttpStatus status = errorCodeStatusMapper.getStatus(errorCode);

        var response = ApplicationResponseDto.failure(
                new GenericDomainException(exception.getMessage(), errorCode), status.value());

        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApplicationResponseDto<Object>> handleValidationException(
            MethodArgumentNotValidException exception) {
        log.error("Validation error", exception);

        FieldError fieldError = exception.getFieldError();
        if (fieldError == null) {
            return handleInvalidErrorKey();
        }

        String enumKey = fieldError.getDefaultMessage();
        ErrorResponseInfo errorInfo = processValidationError(exception, enumKey);
        HttpStatus status = errorCodeStatusMapper.getStatus(errorInfo.errorCode);

        var response = ApplicationResponseDto.failure(
                new GenericDomainException(errorInfo.formattedMessage, errorInfo.errorCode),
                status.value());

        return ResponseEntity.badRequest().body(response);
    }

    private static class ErrorResponseInfo {
        ErrorCode errorCode;
        String formattedMessage;

        ErrorResponseInfo(ErrorCode errorCode, String message) {
            this.errorCode = errorCode;
            this.formattedMessage = message;
        }
    }

    private ErrorResponseInfo processValidationError(MethodArgumentNotValidException exception,
            String enumKey) {
        log.info("Enum key: {}", PREFIX_ERROR + enumKey);
        try {
            ConstraintViolation<?> constraintViolation = exception.getBindingResult().getAllErrors()
                    .get(0).unwrap(ConstraintViolation.class);

            Map<String, Object> attributes = constraintViolation.getConstraintDescriptor()
                    .getAttributes();

            log.info("Attributes: {}", attributes);

            ErrorCode error = ErrorCode.valueOf(enumKey);
            log.info("Error code: {}", error);
            String formattedMessage = messageSource.getMessage(PREFIX_ERROR + enumKey, null,
                    LocaleContextHolder.getLocale());

            if (attributes.containsKey(FIELD_ATTRIBUTE)) {
                String requiredField = attributes.get(FIELD_ATTRIBUTE).toString();
                formattedMessage = formatRequiredMessage(requiredField);
            }

            if (attributes.containsKey(VALUES_ATTRIBUTE)) {
                String[] values = (String[]) attributes.get(VALUES_ATTRIBUTE);
                formattedMessage = formatEnumValues(formattedMessage, values);
            }

            if (attributes.containsKey(MIN_VALUE_ATTRIBUTE)) {
                String value = String.valueOf(attributes.get(MIN_VALUE_ATTRIBUTE));
                formattedMessage = formatMinValue(formattedMessage, value);
            }

            return new ErrorResponseInfo(error, formattedMessage);
        } catch (Exception e) {
            log.debug("Error processing validation error", e);
            return new ErrorResponseInfo(ErrorCode.INVALID_ERROR_KEY,
                    ErrorCode.INVALID_ERROR_KEY.getDefaultMessage());
        }
    }

    private ResponseEntity<ApplicationResponseDto<Object>> handleInvalidErrorKey() {
        ErrorCode errorCode = ErrorCode.INVALID_ERROR_KEY;
        return ResponseEntity.badRequest().body(ApplicationResponseDto
                .failure(new GenericDomainException(errorCode.getDefaultMessage(), errorCode)));
    }

    private String formatEnumValues(String message, String[] values) {
        return message.replace(String.format("{%s}", VALUES_ATTRIBUTE), Arrays.toString(values));
    }

    private String formatRequiredMessage(String requiredField) {
        return String.format("%s is required", requiredField);
    }

    private String formatMinValue(String message, String minValue) {
        log.info("Min value: {}", minValue);
        log.info("Message: {}", message);
        return message.replace(String.format("{%s}", MIN_VALUE_ATTRIBUTE), minValue);
    }
}