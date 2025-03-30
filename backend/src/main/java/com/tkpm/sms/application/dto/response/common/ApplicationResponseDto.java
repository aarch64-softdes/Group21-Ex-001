package com.tkpm.sms.application.dto.response.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.tkpm.sms.domain.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Data
@Builder(toBuilder = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApplicationResponseDto<T> {
    @Builder.Default
    int code = HttpStatus.OK.value();

    String message;
    String errorCode;
    T content;

    public static <T> ApplicationResponseDto<T> success(T content, String message) {
        return ApplicationResponseDto.<T>builder()
                .message(message)
                .content(content)
                .build();
    }

    public static <T> ApplicationResponseDto<T> success(T content) {
        return ApplicationResponseDto.<T>builder()
                .content(content)
                .build();
    }

    public static <T> ApplicationResponseDto<T> success(String message) {
        return ApplicationResponseDto.<T>builder()
                .message(message)
                .content(null)
                .build();
    }


    public static <T> ApplicationResponseDto<T> success() {
        return ApplicationResponseDto.<T>builder()
                .content(null)
                .build();
    }

    public static <T> ApplicationResponseDto<T> failure(ErrorCode exception) {
        return ApplicationResponseDto.<T>builder()
                .code(500)
                .message(exception.getMessage())
                .errorCode(exception.name())
                .build();
    }


    public static <T> ApplicationResponseDto<T> failure(ErrorCode exception,int status) {
        return ApplicationResponseDto.<T>builder()
                .code(status)
                .message(exception.getMessage())
                .errorCode(exception.name())
                .build();
    }
}