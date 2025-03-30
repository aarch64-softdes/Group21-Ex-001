package com.tkpm.sms.infrastructure.aspect;

import com.tkpm.sms.domain.annotation.LoggingAfter;
import com.tkpm.sms.domain.annotation.LoggingAround;
import com.tkpm.sms.domain.annotation.LoggingBefore;
import com.tkpm.sms.domain.annotation.LoggingException;
import com.tkpm.sms.infrastructure.logging.BaseLogger;
import com.tkpm.sms.infrastructure.logging.LogEntry;
import com.tkpm.sms.infrastructure.logging.LoggerManager;
import com.tkpm.sms.infrastructure.logging.LoggerType;
import com.tkpm.sms.infrastructure.utils.ArgumentFormatterUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {


    @Value("${logging.aspect.logger-type:CONSOLE}")
    private String loggerType;

    private final BaseLogger logger = LoggerManager.getDefaultLoggerStatic();

    @Around("@annotation(loggingAround)")
    public Object logAround(ProceedingJoinPoint joinPoint, LoggingAround loggingAround) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String messageTemplate = loggingAround.value();
        String[] argNames = loggingAround.args();

        Object[] formattedArgs = ArgumentFormatterUtils.getFormattedArgs(joinPoint, argNames);

        // Create metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("method", methodName);
        metadata.put("class", className);

        LogEntry beforeLogEntry = LogEntry.builder()

                .source(className)
                .level(LogLevel.INFO)
                .metadata(metadata)
                .build();

        if (!StringUtils.hasText(messageTemplate)) {
            beforeLogEntry.setMessage(String.format("[BEFORE] Method %s called with arguments %s",
                    methodName, joinPoint.getArgs()));
        } else {
            beforeLogEntry.setMessage("[BEFORE] " + formatMessage(messageTemplate, formattedArgs));
        }

        logger.log(beforeLogEntry);

        Object result = joinPoint.proceed();

        LogEntry afterLogEntry = LogEntry.builder()
                .source(className)
                .level(LogLevel.INFO)
                .metadata(metadata)
                .build();

        if (!StringUtils.hasText(messageTemplate)) {
            afterLogEntry.setMessage(String.format("[AFTER] Method %s returned %s",
                    methodName, result));
        } else {
            afterLogEntry.setMessage("[AFTER] " + formatMessage(messageTemplate, formattedArgs) + " completed");
        }

        logger.log(afterLogEntry);

        return result;
    }

    @Before("@annotation(loggingBefore)")
    public void logBefore(JoinPoint joinPoint, LoggingBefore loggingBefore) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String messageTemplate = loggingBefore.value();
        String[] argNames = loggingBefore.args();

        Object[] formattedArgs = ArgumentFormatterUtils.getFormattedArgs(joinPoint, argNames);

        // Create metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("method", methodName);
        metadata.put("class", className);

        LogEntry logEntry = LogEntry.builder()
                .source(className)
                .level(LogLevel.INFO)
                .metadata(metadata)
                .build();

        if (!StringUtils.hasText(messageTemplate)) {
            logEntry.setMessage(String.format("[BEFORE] Method %s called with arguments %s",
                    methodName, joinPoint.getArgs()));
        } else {
            logEntry.setMessage("[BEFORE] " + formatMessage(messageTemplate, formattedArgs));
        }

        logger.log(logEntry);
    }

    @AfterReturning(pointcut = "@annotation(loggingAfter)", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, LoggingAfter loggingAfter, Object result) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String messageTemplate = loggingAfter.value();
        String[] argNames = loggingAfter.args();

        Object[] formattedArgs = ArgumentFormatterUtils.getFormattedArgs(joinPoint, argNames);

        // Create metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("method", methodName);
        metadata.put("class", className);

        LogEntry logEntry = LogEntry.builder()
                .source(className)
                .level(LogLevel.INFO)
                .metadata(metadata)
                .build();

        if (!StringUtils.hasText(messageTemplate)) {
            logEntry.setMessage(String.format("[AFTER] Method %s returned %s",
                    methodName, result));
        } else {
            logEntry.setMessage("[AFTER] " + formatMessage(messageTemplate, formattedArgs) + " completed");
        }

        logger.log(logEntry);
    }

    @AfterThrowing(pointcut = "@annotation(loggingException)", throwing = "exception")
    public void logAfterThrowing(JoinPoint joinPoint, LoggingException loggingException, Throwable exception) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String messageTemplate = loggingException.value();
        String[] argNames = loggingException.args();

        Object[] formattedArgs = ArgumentFormatterUtils.getFormattedArgs(joinPoint, argNames);

        // Create metadata
        Map<String, Object> metadata = new HashMap<>();
        metadata.put("method", methodName);
        metadata.put("class", className);
        metadata.put("exceptionClass", exception.getClass().getName());
        metadata.put("exceptionMessage", exception.getMessage());

        LogEntry logEntry = LogEntry.builder()
                .source(className)
                .level(LogLevel.ERROR)
                .metadata(metadata)
                .build();

        if (!StringUtils.hasText(messageTemplate)) {
            logEntry.setMessage(String.format("[ERROR] Method %s threw exception %s",
                    methodName, exception));
        } else {
            Object[] argsWithException = new Object[formattedArgs.length + 1];
            System.arraycopy(formattedArgs, 0, argsWithException, 0, formattedArgs.length);
            argsWithException[formattedArgs.length] = exception;

            logEntry.setMessage("[ERROR] " + formatMessage(messageTemplate, argsWithException));
        }

        logger.log(logEntry);
    }

    /**
     * Formats a message by replacing placeholders with argument values
     */
    private String formatMessage(String template, Object[] args) {
        if (args == null || args.length == 0) {
            return template;
        }

        String result = template;
        for (int i = 0; i < args.length; i++) {
            String placeholder = "{}";
            int index = result.indexOf(placeholder);
            if (index != -1) {
                result = result.substring(0, index) +
                        (args[i] == null ? "null" : args[i].toString()) +
                        result.substring(index + placeholder.length());
            }
        }
        return result;
    }
}