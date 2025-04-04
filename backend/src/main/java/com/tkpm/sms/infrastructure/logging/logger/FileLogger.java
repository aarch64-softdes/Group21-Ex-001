package com.tkpm.sms.infrastructure.logging.logger;

import com.tkpm.sms.infrastructure.logging.LogEntry;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FileLogger extends AbstractLogger {
    public FileLogger() {
        super("com.tkpm.sms.logging.logger.FileLogger");
    }

    @Override
    protected String formatMessage(String message, Map<String, Object> metadata) {
        StringBuilder formattedMessage = new StringBuilder(message);

        if (metadata != null && !metadata.isEmpty()) {
            formattedMessage.append(" | ");
            boolean first = true;
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                if (!first) {
                    formattedMessage.append(", ");
                }
                formattedMessage.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
        }

        return formattedMessage.toString();
    }

    @Override
    protected String formatLogEntry(LogEntry logEntry) {
        StringBuilder sb = new StringBuilder();

        // Add timestamp
        sb.append(logEntry.getTimestamp()).append(" | ");

        // Add level
        sb.append(logEntry.getLevel()).append(" | ");

        // Add correlation ID if present
        if (logEntry.getCorrelationId() != null) {
            sb.append(logEntry.getCorrelationId()).append(" | ");
        } else {
            sb.append("-").append(" | ");
        }

        // Add source if present
        if (logEntry.getSource() != null) {
            sb.append(logEntry.getSource()).append(" | ");
        } else {
            sb.append("-").append(" | ");
        }

        // Add message
        sb.append(logEntry.getMessage());

        // Add metadata if present
        Map<String, Object> metadata = logEntry.getMetadata();
        if (metadata != null && !metadata.isEmpty()) {
            sb.append(" | ");
            boolean first = true;
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(entry.getKey()).append("=").append(entry.getValue());
                first = false;
            }
        }

        // Add optional fields if present
        if (logEntry.getDuration() != null) {
            sb.append(" | duration=").append(logEntry.getDuration());
        }

        if (logEntry.getUserId() != null) {
            sb.append(" | userId=").append(logEntry.getUserId());
        }

        if (logEntry.getUserAgent() != null) {
            sb.append(" | userAgent=").append(logEntry.getUserAgent());
        }

        if (logEntry.getIp() != null) {
            sb.append(" | ip=").append(logEntry.getIp());
        }

        return sb.toString();
    }

    @Override
    public void log(LogEntry logEntry) {
        String formattedMessage = formatLogEntry(logEntry);
        logWithLevel(formattedMessage, logEntry.getLevel());
    }
}