package com.tkpm.sms.infrastructure.logging;

import org.springframework.boot.logging.LogLevel;

import java.util.Map;

public interface BaseLogger {
    void log(LogEntry logEntry);
    void log(String message, LogLevel level, Map<String, Object> metadata);
    void log(String message, LogLevel level);
    void log(String message);
    void setLogLevel(LogLevel level);
    LogLevel getLogLevel();
}
