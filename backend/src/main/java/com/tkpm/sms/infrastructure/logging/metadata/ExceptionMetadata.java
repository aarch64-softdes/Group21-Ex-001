package com.tkpm.sms.infrastructure.logging.metadata;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

/**
 * Metadata for exception logging
 */
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExceptionMetadata extends BaseMetadata {
    String controller;
    String method;
    long executionTime;
    String endpoint;
    String path;
    String httpMethod;
    String exceptionClass;
    String exceptionMessage;
    String requestParameters;

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", controller);
        map.put("method", method);
        map.put("executionTime", executionTime);
        map.put("endpoint", endpoint);
        map.put("path", path);
        map.put("httpMethod", httpMethod);
        map.put("exceptionClass", exceptionClass);
        map.put("exceptionMessage", exceptionMessage);
        map.put("requestParameters", requestParameters);
        return map;
    }
}