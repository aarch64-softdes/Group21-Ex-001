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

        // Add all required fields with snake_case naming
        logData.put("timestamp", logEntry.getTimestamp());
        logData.put("level", logEntry.getLevel().toString());
        logData.put("message", logEntry.getMessage());

        // Add correlation ID (required, use empty if null)
        logData.put("correlation_id", logEntry.getCorrelationId() != null ?
                logEntry.getCorrelationId() : "");

        // Add source (required, use empty if null)
        logData.put("source", logEntry.getSource() != null ?
                logEntry.getSource() : "");

        // Add optional fields if present with snake_case naming
        if (logEntry.getUserId() != null) {
            logData.put("user_id", logEntry.getUserId());
        }

        if (logEntry.getDuration() != null) {
            logData.put("duration_ms", logEntry.getDuration());
        }

        if (logEntry.getUserAgent() != null) {
            logData.put("user_agent", logEntry.getUserAgent());
        }

        if (logEntry.getIp() != null) {
            logData.put("client_ip", logEntry.getIp());
        }

        // Add metadata fields directly to the root level instead of nesting
        if (logEntry.getMetadata() != null && !logEntry.getMetadata().isEmpty()) {
            for (Map.Entry<String, Object> entry : logEntry.getMetadata().entrySet()) {
                String key = convertToSnakeCase(entry.getKey());
                logData.put(key, entry.getValue());
            }
        }

        return JsonUtils.toJson(logData);
    }

    /**
     * Convert camelCase to snake_case
     */
    private String convertToSnakeCase(String camelCase) {
        if (camelCase == null || camelCase.isEmpty()) {
            return camelCase;
        }

        StringBuilder result = new StringBuilder();
        result.append(Character.toLowerCase(camelCase.charAt(0)));

        for (int i = 1; i < camelCase.length(); i++) {
            char ch = camelCase.charAt(i);
            if (Character.isUpperCase(ch)) {
                result.append('_');
                result.append(Character.toLowerCase(ch));
            } else {
                result.append(ch);
            }
        }

        return result.toString();
    }

    @Override
    public void log(LogEntry logEntry) {
        String formattedMessage = formatLogEntry(logEntry);
        logWithLevel(formattedMessage, logEntry.getLevel());
    }
}