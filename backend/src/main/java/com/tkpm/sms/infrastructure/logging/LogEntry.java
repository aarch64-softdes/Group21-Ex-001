package com.tkpm.sms.infrastructure.logging;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.logging.LogLevel;

/**
 * Standard log entry format with all required fields for the logging system.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LogEntry {
    String timestamp;
    LogLevel level;
    String correlationId;
    String userId;
    String source;
    String message;
    Map<String, Object> metadata;
    Long duration;
    String userAgent;
    String ip;

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

    /**
     * Convert log entry to a map for easy serialization
     *
     * @return Map of log entry fields
     */
    public Map<String, Object> toHashMap() {
        Map<String, Object> document = new HashMap<>();

        // Add standard fields with snake_case naming
        document.put("timestamp", this.timestamp);
        document.put("level", this.level.toString());
        document.put("correlation_id", this.correlationId);
        document.put("message", this.message);
        document.put("source", this.source);

        // Add optional fields if present
        if (this.userId != null) {
            document.put("user_id", this.userId);
        }

        if (this.duration != null) {
            document.put("duration_ms", this.duration);
        }

        if (this.userAgent != null) {
            document.put("user_agent", this.userAgent);
        }

        if (this.ip != null) {
            document.put("client_ip", this.ip);
        }

        if (this.metadata != null) {
            document.put("metadata", this.metadata);
        }

        return document;
    }
}