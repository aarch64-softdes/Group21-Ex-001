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
        return JsonUtils.toJson(logEntry.toHashMap());
    }

    @Override
    public void log(LogEntry logEntry) {
        String formattedMessage = formatLogEntry(logEntry);
        logWithLevel(formattedMessage, logEntry.getLevel());
    }
}