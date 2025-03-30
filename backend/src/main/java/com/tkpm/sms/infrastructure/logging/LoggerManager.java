package com.tkpm.sms.infrastructure.logging;

import com.tkpm.sms.infrastructure.logging.logger.ConsoleLogger;
import com.tkpm.sms.infrastructure.logging.logger.ElasticsearchLogger;
import com.tkpm.sms.infrastructure.logging.logger.FileLogger;
import com.tkpm.sms.infrastructure.logging.logger.JsonLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class LoggerManager {
    private static LoggerManager instance;
    private final ApplicationContext applicationContext;

    @Autowired
    public LoggerManager(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        instance = this;
    }

    @Value("${logging.default-type:CONSOLE}")
    private String defaultLoggerType;

    /**
     * Get a logger by type
     */
    public BaseLogger getLogger(LoggerType type) {
        switch (type) {
            case FILE:
                return applicationContext.getBean(FileLogger.class);
            case JSON:
                return applicationContext.getBean(JsonLogger.class);
            case ELASTICSEARCH:
                return applicationContext.getBean(ElasticsearchLogger.class);
            case CONSOLE:
            default:
                return applicationContext.getBean(ConsoleLogger.class);
        }
    }

    /**
     * Get a logger by class
     */
    public BaseLogger getLogger(Class<?> clazz) {
        if (clazz == FileLogger.class) {
            return applicationContext.getBean(FileLogger.class);
        } else if (clazz == JsonLogger.class) {
            return applicationContext.getBean(JsonLogger.class);
        } else if (clazz == ElasticsearchLogger.class) {
            return applicationContext.getBean(ElasticsearchLogger.class);
        } else {
            return applicationContext.getBean(ConsoleLogger.class);
        }
    }

    /**
     * Get the default logger as configured
     */
    public BaseLogger getDefaultLogger() {
        try {
            LoggerType type = LoggerType.valueOf(defaultLoggerType);
            return getLogger(type);
        } catch (IllegalArgumentException e) {
            // Fallback to console logger if configuration is invalid
            return getLogger(LoggerType.CONSOLE);
        }
    }

    /**
     * Static method to get a logger by type
     */
    public static BaseLogger getLoggerStatic(LoggerType type) {
        return instance.getLogger(type);
    }

    /**
     * Create a new LogEntry builder with source set to the caller class
     */
    public static LogEntry createLogEntry(String source) {
        return LogEntry.builder().source(source).build();
    }
}