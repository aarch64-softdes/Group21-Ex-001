package com.tkpm.sms.logging.metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Metadata for exception logging
 */
@Builder
@Getter
@Setter
public class ExceptionMetadata extends BaseMetadata {
    private String controller;
    private String method;
    private long executionTime;
    private String endpoint;
    private String path;
    private String httpMethod;
    private String exceptionClass;
    private String exceptionMessage;

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
        return map;
    }
}