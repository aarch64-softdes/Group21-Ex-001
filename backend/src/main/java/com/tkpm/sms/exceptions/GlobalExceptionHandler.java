package com.tkpm.sms.exceptions;

import com.tkpm.sms.enums.Faculty;
import com.tkpm.sms.enums.Gender;
import com.tkpm.sms.enums.Status;
import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();

        assert errorMessage != null;
        return ResponseEntity.badRequest().body(Map.of("error", errorMessage));
    }
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidEnumValue(HttpMessageNotReadableException ex) {
        String message = ex.getMostSpecificCause().getMessage();

        // Xác định enum nào đang bị lỗi
        if (message.contains("Status")) {
            return buildErrorResponse("status", Status.class);
        } else if (message.contains("Gender")) {
            return buildErrorResponse("gender", Gender.class);
        } else if (message.contains("Faculty")) {
            return buildErrorResponse("faculty", Faculty.class);
        }

        return ResponseEntity.badRequest().body(Map.of("error", "Invalid input value."));
    }

    private <E extends Enum<E>> ResponseEntity<Map<String, Object>> buildErrorResponse(String field, Class<E> enumClass) {
        List<String> validValues = Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("error", "Invalid value for field '" + field + "'.");
        response.put("allowed_values", validValues);

        return ResponseEntity.badRequest().body(response);
    }
}

