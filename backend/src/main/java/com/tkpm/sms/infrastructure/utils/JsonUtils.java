package com.tkpm.sms.infrastructure.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Converts an object to a JSON string.
     *
     * @param object The object to convert
     * @return JSON string representation of the object
     */
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    /**
     * Parses a JSON string into an object of the specified type.
     *
     * @param json      The JSON string to parse
     * @param valueType The class of the object to parse the JSON into
     * @param <T>       The type of the object
     * @return The parsed object
     */
    public static <T> T fromJson(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to parse JSON", e);
        }
    }
}