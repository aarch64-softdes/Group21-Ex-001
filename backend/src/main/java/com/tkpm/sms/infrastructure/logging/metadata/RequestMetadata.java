package com.tkpm.sms.infrastructure.logging.metadata;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.HashMap;
import java.util.Map;

/**
 * Metadata for controller request logging
 */
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequestMetadata extends BaseMetadata {
    String controller;
    String method;
    String endpoint;
    String path;
    String httpMethod;
    String parameters;

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