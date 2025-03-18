package com.tkpm.sms.logging;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import com.tkpm.sms.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.logging.LogLevel;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class ElasticsearchLogger extends AbstractLogger {
    private final ElasticsearchClient esClient;
    private final String indexPrefix;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ElasticsearchLogger(
            ElasticsearchClient esClient,
            @Value("${elasticsearch.index-prefix:logs}") String indexPrefix){
        super("com.tkpm.sms.logging.ElasticsearchLogger");
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

        return JsonUtils.toJson(logData);
    }

    @Override
    public void log(String message, LogLevel level, Map<String, Object> metadata) {
        // First, let the abstract logger handle standard logging via SLF4J
        // This will respect the logback.xml configuration
        super.log(message, level, metadata);

        // Additionally, log directly to Elasticsearch
        try {
            Map<String, Object> logData = createLogData(message, level, metadata);
            String indexName = getIndexName();

            IndexResponse response = esClient.index(i -> i
                    .index(indexName)
                    .document(logData)
            );

             logger.debug("Document indexed with ID: " + response.id());

        } catch (IOException e) {
            // If ES logging fails, log the error through SLF4J
            logger.error("Failed to log to Elasticsearch: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> createLogData(String message, LogLevel level, Map<String, Object> metadata) {
        Map<String, Object> logData = new HashMap<>();
        logData.put("message", message);
        logData.put("level", level.toString());
        logData.put("timestamp", LocalDateTime.now().toString());
        logData.put("logger", "ElasticsearchLogger");

        if (metadata != null && !metadata.isEmpty()) {
            logData.put("metadata", metadata);
        }

        return logData;
    }

    private String getIndexName() {
        // Format: logs-YYYY-MM-DD
        return indexPrefix + "-" + LocalDateTime.now().format(dateFormatter);
    }
}