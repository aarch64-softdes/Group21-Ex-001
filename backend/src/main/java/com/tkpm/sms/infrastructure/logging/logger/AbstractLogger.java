package com.tkpm.sms.infrastructure.logging.logger;

import com.tkpm.sms.infrastructure.logging.BaseLogger;
import com.tkpm.sms.infrastructure.logging.LogEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;

import java.util.Map;

public abstract class AbstractLogger implements BaseLogger {
    protected final Logger logger;
    private LogLevel currentLevel;
    protected final String loggerName;

    protected AbstractLogger(String loggerName) {
        this.logger = LoggerFactory.getLogger(loggerName);
        this.currentLevel = LogLevel.INFO; // Default level
        this.loggerName = loggerName;
    }

    @Override
    public void setLogLevel(LogLevel level) {
        this.currentLevel = level;
    }

    @Override
    public LogLevel getLogLevel() {
        return currentLevel;
    }

    @Override
    public void log(LogEntry logEntry) {
        String formattedMessage = formatLogEntry(logEntry);
        logWithLevel(formattedMessage, logEntry.getLevel());
    }

    protected void logWithLevel(String message, LogLevel level, Map<String, Object> metadata) {
        String formattedMessage = formatMessage(message, metadata);
        logWithLevel(formattedMessage, level);
    }

    protected void logWithLevel(String formattedMessage, LogLevel level) {
        if (level.ordinal() < currentLevel.ordinal()) {
            return; // Skip logging if below current level
        }

        switch (level) {
            case DEBUG:
                if (logger.isDebugEnabled()) logger.debug(formattedMessage);
                break;
            case WARN:
                if (logger.isWarnEnabled()) logger.warn(formattedMessage);
                break;
            case ERROR:
                if (logger.isErrorEnabled()) {
                    logger.error(formattedMessage);
                }
                break;
            case INFO:
            default:
                if (logger.isInfoEnabled()) logger.info(formattedMessage);
                break;
        }
    }

    @Override
    public void log(String message, LogLevel level, Map<String, Object> metadata) {
        LogEntry logEntry = LogEntry.builder()
                .level(level)
                .message(message)
                .metadata(metadata)
                .source(loggerName)
                .build();
        log(logEntry);
    }

    @Override
    public void log(String message, LogLevel level) {
        log(message, level, null);
    }

    @Override
    public void log(String message) {
        log(message, LogLevel.INFO);
    }

    /**
     * Format a simple message with metadata
     */
    protected abstract String formatMessage(String message, Map<String, Object> metadata);

    /**
     * Format a complete log entry
     */
    protected abstract String formatLogEntry(LogEntry logEntry);
}