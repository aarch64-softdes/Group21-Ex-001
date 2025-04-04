package com.tkpm.sms.infrastructure.logging.logger;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.tkpm.sms.infrastructure.logging.LogEntry;
import com.tkpm.sms.infrastructure.utils.JsonUtils;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ElasticsearchLogger extends AbstractLogger {
    ElasticsearchClient esClient;
    String indexPrefix;
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

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
            logData.put("metadata", metadata);
        }

        return JsonUtils.toJson(logData);
    }

    @Override
    protected String formatLogEntry(LogEntry logEntry) {
        // Reuse the prepareElasticsearchDocument method to avoid code duplication
        Map<String, Object> logData = logEntry.toHashMap();
        return JsonUtils.toJson(logData);
    }

    @Override
    public void log(LogEntry logEntry) {
        // Convert LogEntry to the expected format
        Map<String, Object> esDocument = logEntry.toHashMap();

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

    private String getIndexName() {
        // Format: logs-YYYY-MM-DD
        return indexPrefix + "-" + LocalDateTime.now().format(dateFormatter);
    }
}