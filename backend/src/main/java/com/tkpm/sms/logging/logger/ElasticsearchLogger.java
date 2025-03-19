package com.tkpm.sms.logging.logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.tkpm.sms.logging.LogEntry;
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
            logData.putAll(metadata);
        }

        return com.tkpm.sms.utils.JsonUtils.toJson(logData);
    }

    @Override
    protected String formatLogEntry(LogEntry logEntry) {
        return "";
    }

    @Override
    public void log(LogEntry logEntry) {
        // First, let the abstract logger handle standard logging via SLF4J
        // Use the parent class's log method to avoid recursion
        super.logWithLevel(formatMessage(logEntry.getMessage(), logEntry.getMetadata()),
                logEntry.getLevel());

        // Additionally, log directly to Elasticsearch
        try {
            String indexName = getIndexName();

            IndexResponse response = esClient.index(i -> i
                    .index(indexName)
                    .id(UUID.randomUUID().toString())
                    .document(logEntry)
                    .opType(co.elastic.clients.elasticsearch._types.OpType.Create)
            );
        } catch (IOException e) {
            // If ES logging fails, log the error through SLF4J
            logger.error("Failed to log to Elasticsearch: " + e.getMessage(), e);
        }
    }

    private String getIndexName() {
        // Format: logs-YYYY-MM-DD
        return indexPrefix + "-" + LocalDateTime.now().format(dateFormatter);
    }
}