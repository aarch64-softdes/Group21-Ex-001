package com.tkpm.sms.logging;

import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class FileLogger extends AbstractLogger {
    public FileLogger() {
        super("com.tkpm.sms.logging.FileLogger");
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
    public void log(String message, LogLevel level, Map<String, Object> metadata) {
        logWithLevel(message, level, metadata);
    }
}
