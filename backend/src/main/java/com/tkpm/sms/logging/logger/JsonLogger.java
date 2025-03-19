package com.tkpm.sms.logging.logger;

import com.tkpm.sms.logging.LogEntry;
import com.tkpm.sms.utils.JsonUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class JsonLogger extends AbstractLogger {
    public JsonLogger() {
        super("com.tkpm.sms.logging.logger.JsonLogger");
    }

    @Override
    protected String formatMessage(String message, Map<String, Object> metadata) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("message", message);

        if (metadata != null && !metadata.isEmpty()) {
            logData.putAll(metadata);
        }

        return JsonUtils.toJson(logData);
    }

    @Override
    protected String formatLogEntry(LogEntry logEntry) {
        Map<String, Object> logData = new HashMap<>();

        // Add all required fields
        logData.put("timestamp", logEntry.getTimestamp());
        logData.put("level", logEntry.getLevel().toString());
        logData.put("message", logEntry.getMessage());

        // Add correlation ID (required, use empty if null)
        logData.put("correlationId", logEntry.getCorrelationId() != null ?
                logEntry.getCorrelationId() : "");

        // Add source (required, use empty if null)
        logData.put("source", logEntry.getSource() != null ?
                logEntry.getSource() : "");

        // Add optional fields if present
        if (logEntry.getUserId() != null) {
            logData.put("userId", logEntry.getUserId());
        }

        if (logEntry.getDuration() != null) {
            logData.put("duration", logEntry.getDuration());
        }

        if (logEntry.getUserAgent() != null) {
            logData.put("userAgent", logEntry.getUserAgent());
        }

        if (logEntry.getIp() != null) {
            logData.put("ip", logEntry.getIp());
        }

        // Add metadata as a nested object if present
        if (logEntry.getMetadata() != null && !logEntry.getMetadata().isEmpty()) {
            logData.put("metadata", logEntry.getMetadata());
        }

        return JsonUtils.toJson(logData);
    }

    @Override
    public void log(LogEntry logEntry) {
        String formattedMessage = formatLogEntry(logEntry);
        logWithLevel(formattedMessage, logEntry.getLevel());
    }
}