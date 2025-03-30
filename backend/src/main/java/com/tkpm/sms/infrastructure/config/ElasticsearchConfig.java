package com.tkpm.sms.infrastructure.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ElasticsearchConfig {
    @Value("${elasticsearch.host}")
    String host;

    @Value("${elasticsearch.port}")
    int port;

    @Value("${elasticsearch.api-key}")
    String apiKey;

    @Bean
    public RestClient restClient() {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(host, port, "http"));

        List<Header> defaultHeaders = new ArrayList<>();
        defaultHeaders.add(new BasicHeader("Authorization", "ApiKey " + apiKey));

        builder.setDefaultHeaders(defaultHeaders.toArray(new Header[0]));

        return builder.build();
    }

    @Bean
    public ElasticsearchTransport elasticsearchTransport(RestClient restClient) {
        return new RestClientTransport(
                restClient,
                new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }
}