package com.tkpm.sms.logging;

import com.tkpm.sms.utils.JsonUtils;
import org.springframework.boot.logging.LogLevel;

import java.util.HashMap;
import java.util.Map;

public class JsonLogger extends AbstractLogger {
    public JsonLogger() {
        super("com.tkpm.sms.logging.JsonLogger");
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
    public void log(String message, LogLevel level, Map<String, Object> metadata) {
        logWithLevel(message, level, metadata);
    }
}