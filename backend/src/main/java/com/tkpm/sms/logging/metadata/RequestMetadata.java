package com.tkpm.sms.logging.metadata;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * Metadata for controller request logging
 */
@Builder
@Getter
@Setter
public class RequestMetadata extends BaseMetadata {
    private String controller;
    private String method;
    private String endpoint;
    private String path;
    private String httpMethod;
    private String parameters;

    @Override
    public Map<String, Object> toHashMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("controller", controller);
        map.put("method", method);
        map.put("endpoint", endpoint);
        map.put("path", path);
        map.put("httpMethod", httpMethod);
        if (parameters != null) {
            map.put("parameters", parameters);
        }
        return map;
    }
}