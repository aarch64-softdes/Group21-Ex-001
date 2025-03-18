package com.tkpm.sms.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;

import java.util.Map;

public abstract class AbstractLogger implements BaseLogger {
    protected final Logger logger;
    private LogLevel currentLevel;

    protected AbstractLogger(String loggerName) {
        this.logger = LoggerFactory.getLogger(loggerName);
        this.currentLevel = LogLevel.INFO; // Default level
    }

    @Override
    public void setLogLevel(LogLevel level) {
        this.currentLevel = level;
    }

    @Override
    public LogLevel getLogLevel() {
        return currentLevel;
    }

    protected void logWithLevel(String message, LogLevel level, Map<String, Object> metadata) {
        String formattedMessage = formatMessage(message, metadata);

        switch (level) {
            case DEBUG:
                if (logger.isDebugEnabled()) logger.debug(formattedMessage);
                break;
            case WARN:
                if (logger.isWarnEnabled()) logger.warn(formattedMessage);
                break;
            case ERROR:
                if (logger.isErrorEnabled()) logger.error(formattedMessage);
                break;
            case INFO:
            default:
                if (logger.isInfoEnabled()) logger.info(formattedMessage);
                break;
        }
    }

    @Override
    public void log(String message, LogLevel level, Map<String, Object> metadata) {
        logWithLevel(message, level, metadata);
    }

    @Override
    public void log(String message, LogLevel level) {
        log(message, level, null);
    }

    @Override
    public void log(String message) {
        log(message, LogLevel.INFO);
    }

    protected abstract String formatMessage(String message, Map<String, Object> metadata);
}