package com.tkpm.sms.infrastructure.logging.logger;

import com.tkpm.sms.infrastructure.logging.LogEntry;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConsoleLogger extends AbstractLogger {
    static String ANSI_RESET = "\u001B[0m";
    static String ANSI_RED = "\u001B[31m";
    static String ANSI_GREEN = "\u001B[32m";
    static String ANSI_YELLOW = "\u001B[33m";
    static String ANSI_BLUE = "\u001B[34m";

    public ConsoleLogger() {
        super("com.tkpm.sms.logging.logger.ConsoleLogger");
    }

    @Override
    protected String formatMessage(String message, Map<String, Object> metadata) {
        StringBuilder formattedMessage = new StringBuilder(message);

        if (metadata != null && !metadata.isEmpty()) {
            formattedMessage.append(" {");
            boolean first = true;
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                if (!first) {
                    formattedMessage.append(", ");
                }
                formattedMessage.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
            formattedMessage.append("}");
        }

        return formattedMessage.toString();
    }

    @Override
    protected String formatLogEntry(LogEntry logEntry) {
        StringBuilder sb = new StringBuilder();

        // Add timestamp
        sb.append("[").append(logEntry.getTimestamp()).append("] ");

        // Add level
        sb.append("[").append(logEntry.getLevel()).append("] ");

        // Add correlation ID if present
        if (logEntry.getCorrelationId() != null) {
            sb.append("[").append(logEntry.getCorrelationId()).append("] ");
        }

        // Add source if present
        if (logEntry.getSource() != null) {
            sb.append("[").append(logEntry.getSource()).append("] ");
        }

        // Add message
        sb.append(logEntry.getMessage());

        // Add metadata if present
        Map<String, Object> metadata = logEntry.getMetadata();
        if (metadata != null && !metadata.isEmpty()) {
            sb.append(" {");
            boolean first = true;
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
            sb.append("}");
        }

        // Add optional fields if present
        if (logEntry.getDuration() != null) {
            sb.append(" duration=").append(logEntry.getDuration()).append("ms");
        }

        if (logEntry.getUserId() != null) {
            sb.append(" userId=").append(logEntry.getUserId());
        }

        if (logEntry.getUserAgent() != null) {
            sb.append(" userAgent=").append(logEntry.getUserAgent());
        }

        if (logEntry.getIp() != null) {
            sb.append(" ip=").append(logEntry.getIp());
        }

        return sb.toString();
    }

    @Override
    public void log(LogEntry logEntry) {
        // Apply color formatting before passing to SLF4J
        String formattedMessage = formatLogEntry(logEntry);
        String coloredMessage = applyColorForLevel(formattedMessage, logEntry.getLevel());

        // Pass to underlying logger
        logWithLevel(coloredMessage, logEntry.getLevel());
    }

    @Override
    public void log(String message, LogLevel level) {
        // Apply color formatting before passing to SLF4J
        String formattedMessage = formatMessage(message, null);
        String coloredMessage = applyColorForLevel(formattedMessage, level);

        // Pass to underlying logger
        logWithLevel(coloredMessage, level);
    }

    private String applyColorForLevel(String message, LogLevel level) {
        switch (level) {
            case DEBUG:
                return ANSI_BLUE + message + ANSI_RESET;
            case INFO:
                return ANSI_GREEN + message + ANSI_RESET;
            case WARN:
                return ANSI_YELLOW + message + ANSI_RESET;
            case ERROR:
                return ANSI_RED + message + ANSI_RESET;
            default:
                return message;
        }
    }
}