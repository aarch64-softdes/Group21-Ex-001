package com.tkpm.sms.infrastructure.aspect;

import com.tkpm.sms.infrastructure.logging.BaseLogger;
import com.tkpm.sms.infrastructure.logging.LogEntry;
import com.tkpm.sms.infrastructure.logging.LoggerManager;
import com.tkpm.sms.infrastructure.logging.LoggerType;
import com.tkpm.sms.infrastructure.logging.metadata.ExceptionMetadata;
import com.tkpm.sms.infrastructure.logging.metadata.RequestMetadata;
import com.tkpm.sms.infrastructure.logging.metadata.ResponseMetadata;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

/**
 * Enhanced AOP aspect that automatically logs all controller methods, including exceptions
 * Logs detailed information to the configured logger and simplified messages to the console
 */
@Aspect
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ControllerLoggingAspect {
    LoggerManager loggerManager;
    ThreadLocal<Long> startTime = new ThreadLocal<>();
    ThreadLocal<String> correlationId = new ThreadLocal<>();
    BaseLogger consoleLogger;
    String loggerType;

    public ControllerLoggingAspect(
            LoggerManager loggerManager,
            @Value("${logging.controller.logger-type:JSON}") String loggerType
    ) {
        this.loggerManager = loggerManager;
        this.consoleLogger = loggerManager.getLogger(LoggerType.CONSOLE);
        this.loggerType = loggerType;
    }

    /**
     * Pointcut that matches all controllers
     */
    @Pointcut("within(com.tkpm.sms.presentation.controller..*)")
    public void controllerPointcut() {
        // Pointcut for all controllers
    }

    /**
     * Log before controller method execution
     */
    @Before("controllerPointcut()")
    public void logBefore(JoinPoint joinPoint) {
        startTime.set(System.currentTimeMillis());
        correlationId.set(UUID.randomUUID().toString()); // TODO: get correlation ID from request header after updating frontend

        HttpServletRequest request = getCurrentRequest();
        if (Objects.isNull(request)) {
            return;
        }

        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        String fullPath = getFullPath(request);

        // Build request metadata
        RequestMetadata metadata = RequestMetadata.builder()
                .controller(className)
                .method(methodName)
                .endpoint(request.getRequestURI())
                .path(fullPath)
                .httpMethod(request.getMethod())
                .parameters(joinPoint.getArgs().length > 0 ? Arrays.toString(joinPoint.getArgs()) : null)
                .build();

        // Create log entry
        LogEntry logEntry = LogEntry.builder()
                .timestamp(LocalDateTime.now().toString())
                .correlationId(correlationId.get())
                .source(className)
                .level(LogLevel.INFO)
                .message("API Request: " + request.getMethod() + " " + fullPath)
                .metadata(metadata.toHashMap())
                .build();

        if (request.getUserPrincipal() != null) {
            logEntry.setUserId(request.getUserPrincipal().getName());
        }

        logEntry.setIp(request.getRemoteAddr());
        logEntry.setUserAgent(request.getHeader("User-Agent"));

        // Log to primary logger
        getLogger().log(logEntry);

        // Also log simplified message to console
        consoleLogger.log("API Request to: " + request.getMethod() + " " + fullPath, LogLevel.INFO);
    }

    /**
     * Log after controller method execution
     */
    @AfterReturning(pointcut = "controllerPointcut()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        long executionTime = System.currentTimeMillis() - startTime.get();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            return;
        }

        String fullPath = getFullPath(request);

        // Build response metadata
        ResponseMetadata metadata = ResponseMetadata.builder()
                .controller(className)
                .method(methodName)
                .executionTime(executionTime)
                .endpoint(request.getRequestURI())
                .path(fullPath)
                .httpMethod(request.getMethod())
                .build();

        // Create log entry
        LogEntry logEntry = LogEntry.builder()
                .timestamp(LocalDateTime.now().toString())
                .correlationId(correlationId.get())
                .source(className)
                .level(LogLevel.INFO)
                .message("API Response: " + request.getMethod() + " " + request.getRequestURI())
                .metadata(metadata.toHashMap())
                .duration(executionTime)
                .build();

        if (request.getUserPrincipal() != null) {
            logEntry.setUserId(request.getUserPrincipal().getName());
        }

        // Log to primary logger
        getLogger().log(logEntry);

        // Also log simplified message to console
        consoleLogger.log("API Response from: " + request.getMethod() + " " + fullPath +
                " (took " + executionTime + "ms)", LogLevel.INFO);

        // Clean up thread local variables
        cleanupThreadLocals();
    }

    /**
     * Log exceptions thrown by controller methods
     */
    @AfterThrowing(pointcut = "controllerPointcut()", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, Exception exception) {
        // Get the request context
        HttpServletRequest request = getCurrentRequest();
        if (request == null) {
            // No request context, so we'll let the GlobalExceptionHandler handle this
            // Just clean up our thread locals
            cleanupThreadLocals();
            return;
        }

        long executionTime = System.currentTimeMillis() - startTime.get();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        String fullPath = getFullPath(request);

        // Build exception metadata
        ExceptionMetadata metadata = ExceptionMetadata.builder()
                .controller(className)
                .method(methodName)
                .executionTime(executionTime)
                .endpoint(request.getRequestURI())
                .path(fullPath)
                .httpMethod(request.getMethod())
                .exceptionClass(exception.getClass().getName())
                .exceptionMessage(exception.getMessage())
                .build();

        // Create log entry
        LogEntry logEntry = LogEntry.builder()
                .timestamp(LocalDateTime.now().toString())
                .correlationId(correlationId.get())
                .source(className)
                .level(LogLevel.ERROR)
                .message("API Exception: " + request.getMethod() + " " + request.getRequestURI() +
                        " - " + exception.getClass().getSimpleName() + ": " + exception.getMessage())
                .metadata(metadata.toHashMap())
                .duration(executionTime)
                .build();

        if (request.getUserPrincipal() != null) {
            logEntry.setUserId(request.getUserPrincipal().getName());
        }

        logEntry.setIp(request.getRemoteAddr());
        logEntry.setUserAgent(request.getHeader("User-Agent"));

        // Log to primary logger
        getLogger().log(logEntry);

        // Also log simplified error message to console
        consoleLogger.log("API Exception in: " + request.getMethod() + " " + fullPath +
                " - " + exception.getClass().getSimpleName() + ": " + exception.getMessage(), LogLevel.ERROR);

        // Clean up thread local variables
        cleanupThreadLocals();
    }

    /**
     * Get full path from the request URL and query string
     */
    private String getFullPath(HttpServletRequest request) {
        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }
        return requestURL.toString();
    }

    /**
     * Clean up thread local variables to prevent memory leaks
     */
    private void cleanupThreadLocals() {
        startTime.remove();
        correlationId.remove();
    }

    /**
     * Get the current HTTP request
     */
    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Get the configured logger or fall back to JSON logger
     */
    private BaseLogger getLogger() {
        try {
            return loggerManager.getLogger(LoggerType.valueOf(loggerType));
        } catch (IllegalArgumentException e) {
            // Fallback to JSON logger if configuration is invalid
            return loggerManager.getLogger(LoggerType.JSON);
        }
    }
}