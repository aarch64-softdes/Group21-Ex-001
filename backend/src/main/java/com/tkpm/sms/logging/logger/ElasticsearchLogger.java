package com.tkpm.sms.logging.logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.tkpm.sms.logging.LogEntry;
import com.tkpm.sms.utils.JsonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class ElasticsearchLogger extends AbstractLogger {
    private final ElasticsearchClient esClient;
    private final String indexPrefix;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ElasticsearchLogger(
            ElasticsearchClient esClient,
            @Value("${elasticsearch.index-prefix:logs}") String indexPrefix) {
        super("com.tkpm.sms.logging.logger.ElasticsearchLogger");
        this.esClient = esClient;
        this.indexPrefix = indexPrefix;
    }

    @Override
    protected String formatMessage(String message, Map<String, Object> metadata) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("message", message);
        logData.put("timestamp", LocalDateTime.now().toString());

        if (metadata != null && !metadata.isEmpty()) {
            // Convert metadata keys to snake_case and add directly to root
            for (Map.Entry<String, Object> entry : metadata.entrySet()) {
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
    protected String formatLogEntry(LogEntry logEntry) {
        // Reuse the prepareElasticsearchDocument method to avoid code duplication
        Map<String, Object> logData = prepareElasticsearchDocument(logEntry);
        return JsonUtils.toJson(logData);
    }

    @Override
    public void log(LogEntry logEntry) {
        // Convert LogEntry to the expected format
        Map<String, Object> esDocument = prepareElasticsearchDocument(logEntry);

        try {
            String indexName = getIndexName();

            IndexResponse response = esClient.index(i -> i
                    .index(indexName)
                    .id(UUID.randomUUID().toString())
                    .document(esDocument)
                    .opType(co.elastic.clients.elasticsearch._types.OpType.Create)
            );

            // Log success via standard logger
            String formattedMessage = formatLogEntry(logEntry);
            logWithLevel(formattedMessage, logEntry.getLevel());

        } catch (IOException e) {
            // If ES logging fails, log the error through SLF4J
            logger.error("Failed to log to Elasticsearch: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> prepareElasticsearchDocument(LogEntry logEntry) {
        Map<String, Object> document = new HashMap<>();

        // Add standard fields with snake_case naming
        document.put("timestamp", logEntry.getTimestamp());
        document.put("level", logEntry.getLevel().toString());
        document.put("correlation_id", logEntry.getCorrelationId());
        document.put("message", logEntry.getMessage());
        document.put("source", logEntry.getSource());

        // Add optional fields if present
        if (logEntry.getUserId() != null) {
            document.put("user_id", logEntry.getUserId());
        }

        if (logEntry.getDuration() != null) {
            document.put("duration_ms", logEntry.getDuration());
        }

        if (logEntry.getUserAgent() != null) {
            document.put("user_agent", logEntry.getUserAgent());
        }

        if (logEntry.getIp() != null) {
            document.put("client_ip", logEntry.getIp());
        }

        // Add metadata as direct fields
        if (logEntry.getMetadata() != null) {
            for (Map.Entry<String, Object> entry : logEntry.getMetadata().entrySet()) {
                document.put(convertToSnakeCase(entry.getKey()), entry.getValue());
            }
        }

        return document;
    }

    private String getIndexName() {
        // Format: logs-YYYY-MM-DD
        return indexPrefix + "-" + LocalDateTime.now().format(dateFormatter);
    }
}