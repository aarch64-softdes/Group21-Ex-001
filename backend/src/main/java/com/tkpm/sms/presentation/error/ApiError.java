package com.tkpm.sms.presentation.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private String errorCode;
    private String message;
    private LocalDateTime timestamp;

    public static ApiError of(String errorCode, String message) {
        return ApiError.builder()
                .errorCode(errorCode)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}