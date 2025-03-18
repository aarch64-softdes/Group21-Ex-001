package com.tkpm.sms.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.util.Map;

public class ConsoleLogger extends AbstractLogger {
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";

    public ConsoleLogger() {
        super("com.tkpm.sms.logging.ConsoleLogger");
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
    public void log(String message, LogLevel level, Map<String, Object> metadata) {
        // Apply color formatting before passing to SLF4J
        String coloredMessage = applyColorForLevel(message, level);
        Map<String, Object> coloredMetadata = metadata;

        // Pass to underlying logger
        logWithLevel(coloredMessage, level, coloredMetadata);
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
