package com.tkpm.sms.aspect;

import com.tkpm.sms.enums.LoggerType;
import com.tkpm.sms.logging.BaseLogger;
import com.tkpm.sms.logging.LogEntry;
import com.tkpm.sms.logging.LoggerManager;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Enhanced AOP aspect that automatically logs all controller methods, including exceptions
 * Logs detailed information to the configured logger and simplified messages to the console
 */
@Aspect
@Component
public class ControllerLoggingAspect {

    private final LoggerManager loggerManager;
    private final ThreadLocal<Long> startTime = new ThreadLocal<>();
    private final ThreadLocal<String> correlationId = new ThreadLocal<>();
    private final BaseLogger consoleLogger;

    @Value("${logging.controller.logger-type:JSON}")
    private String loggerType;

    public ControllerLoggingAspect(LoggerManager loggerManager) {
        this.loggerManager = loggerManager;
        this.consoleLogger = loggerManager.getLogger(LoggerType.CONSOLE);
    }

    /**
     * Pointcut that matches all controllers
     */
    @Pointcut("within(com.tkpm.sms.controller..*)")
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
        if (request == null) {
            return;
        }
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }
        String fullPath = requestURL.toString();

        // Build metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("controller", className);
        metadata.put("method", methodName);
        metadata.put("endpoint", request.getRequestURI());
        metadata.put("path", fullPath);
        metadata.put("httpMethod", request.getMethod());

        // Add parameters if present
        if (joinPoint.getArgs().length > 0) {
            metadata.put("parameters", Arrays.toString(joinPoint.getArgs()));
        }

        // Create log entry
        LogEntry logEntry = LogEntry.builder()
                .correlationId(correlationId.get())
                .source(className)
                .level(LogLevel.INFO)
                .message("API Request: " + request.getMethod() + " " + fullPath)
                .metadata(metadata)
                .build();

        if (request.getUserPrincipal() != null) {
            logEntry.setUserId(request.getUserPrincipal().getName());
        }

        logEntry.setIp(request.getRemoteAddr());
        logEntry.setUserAgent(request.getHeader("User-Agent"));

        // Log to primary logger
        getLogger().log(logEntry);

        // Also log simplified message to console
        consoleLogger.log("API Request to: " + request.getMethod() + " " + fullPath, LogLevel.INFO);    }

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

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }
        String fullPath = requestURL.toString();

        // Build metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("controller", className);
        metadata.put("method", methodName);
        metadata.put("executionTime", executionTime);
        metadata.put("endpoint", request.getRequestURI());
        metadata.put("path", fullPath);
        metadata.put("httpMethod", request.getMethod());

        // Create log entry
        LogEntry logEntry = LogEntry.builder()
                .correlationId(correlationId.get())
                .source(className)
                .level(LogLevel.INFO)
                .message("API Response: " + request.getMethod() + " " + request.getRequestURI())
                .metadata(metadata)
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
        startTime.remove();
        correlationId.remove();
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
            if (startTime.get() != null) {
                startTime.remove();
            }
            if (correlationId.get() != null) {
                correlationId.remove();
            }
            return;
        }

        long executionTime = System.currentTimeMillis() - startTime.get();
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();

        StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
        String queryString = request.getQueryString();
        if (queryString != null) {
            requestURL.append("?").append(queryString);
        }
        String fullPath = requestURL.toString();

        // Build metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("controller", className);
        metadata.put("method", methodName);
        metadata.put("executionTime", executionTime);
        metadata.put("endpoint", request.getRequestURI());
        metadata.put("path", fullPath);
        metadata.put("httpMethod", request.getMethod());
        metadata.put("exceptionClass", exception.getClass().getName());
        metadata.put("exceptionMessage", exception.getMessage());

        // Create log entry
        LogEntry logEntry = LogEntry.builder()
                .correlationId(correlationId.get())
                .source(className)
                .level(LogLevel.ERROR)
                .message("API Exception: " + request.getMethod() + " " + request.getRequestURI() +
                        " - " + exception.getClass().getSimpleName() + ": " + exception.getMessage())
                .metadata(metadata)
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

    private BaseLogger getLogger() {
        try {
            return loggerManager.getLogger(LoggerType.valueOf(loggerType));
        } catch (IllegalArgumentException e) {
            // Fallback to JSON logger if configuration is invalid
            return loggerManager.getLogger(LoggerType.JSON);
        }
    }
}