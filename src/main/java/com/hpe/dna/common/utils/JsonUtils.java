package com.hpe.dna.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hpe.dna.common.AppRuntimeException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Utilities for Jackson
 * @author chun-yang.wang@hpe.com
 */
public class JsonUtils {
    private static ObjectMapper objectMapper = new ObjectMapper();

    public static String asJson(Map<String, String> map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new AppRuntimeException("Failed convert map to json string", e);

        }
    }

    public static Map<String, Object> asMap(String json) {
        JavaType type = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
        try {
            return objectMapper.readValue(json, type);
        } catch (IOException e) {
            throw new AppRuntimeException("Failed convert JSON string to map", e);
        }
    }

    public static <T> List<T> asList(Class<?> classType, String json) {
        try {
            JavaType _type = objectMapper.getTypeFactory().constructCollectionType(List.class, classType);
            return objectMapper.readValue(json, _type);
        } catch (IOException e) {
            throw new AppRuntimeException("Failed convert JSON string to list", e);
        }
    }
}
