package com.tkpm.sms.infrastructure.logging.metadata;

import java.util.Map;

public abstract class BaseMetadata {
    /**
     * Convert metadata to HashMap for logging
     * @return Map containing metadata fields
     */
    public abstract Map<String, Object> toHashMap();
}
