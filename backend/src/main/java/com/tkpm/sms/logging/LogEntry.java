package com.tkpm.sms.logging;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.*;
import org.springframework.boot.logging.LogLevel;

/**
 * Standard log entry format with all required fields for the logging system.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
public class LogEntry {
    private String timestamp;
    private LogLevel level;
    private String correlationId;
    private String userId;
    private String source;
    private String message;
    private Map<String, Object> metadata;
    private Long duration;
    private String userAgent;
    private String ip;

    // For performance tracking
    private long startTimeMillis;

    private LogEntry() {
        // Set default values
        this.timestamp = ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        this.level = LogLevel.INFO;
        this.correlationId = UUID.randomUUID().toString();
        this.metadata = new HashMap<>();
        this.startTimeMillis = System.currentTimeMillis();
    }

    /**
     * Calculate duration based on start time
     */
    public void endTracking() {
        this.duration = System.currentTimeMillis() - startTimeMillis;
    }

    /**
     * Reset start time for tracking
     */
    public void resetTracking() {
        this.startTimeMillis = System.currentTimeMillis();
    }
}