package com.tkpm.sms.logging.metadata;

import lombok.Builder;

import java.util.HashMap;
import java.util.Map;

/**
 * Metadata for controller response logging
 */
@Builder
public class ResponseMetadata extends BaseMetadata {
    private String controller;
    private String method;
    private long executionTime;
    private String endpoint;
    private String path;
    private String httpMethod;

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", controller);
        map.put("method", method);
        map.put("executionTime", executionTime);
        map.put("endpoint", endpoint);
        map.put("path", path);
        map.put("httpMethod", httpMethod);
        return map;
    }
}
