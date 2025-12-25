package com.abhinav.framework.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * -----------------------------------------------------------------------------
 * File: JsonUtil.java
 * Purpose: Wrapper for Jackson ObjectMapper to handle JSON operations.
 *
 * Why:
 * - Simplifies conversion between Java Objects and JSON Strings.
 * - Centralizes JSON configuration (e.g., ignoring unknown properties).
 * -----------------------------------------------------------------------------
 */
public class JsonUtil {

    // Global ObjectMapper config: Do NOT fail if the JSON contains extra fields we
    // don't know about.
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    // Converts a Java Object (e.g., UserRequest) into a JSON String.
    public static String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to convert object to JSON", e);
        }
    }

    // Converts a JSON String back into a Java Object (e.g., UserResponse).
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert JSON to Object", e);
        }
    }
}
