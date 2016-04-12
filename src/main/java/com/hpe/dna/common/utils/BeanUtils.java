package com.hpe.dna.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

/**
 * @author chun-yang.wang@hpe.com
 */
public class BeanUtils {
    private static ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> toMap(Object pojoValue) {
        Map<String, Object> propertyMap = mapper.convertValue(pojoValue, Map.class);
        return propertyMap;
    }

    public static Map<String, Object> injectFields(Object pojoValue, Map<String, Object> extraFields) {
        Map<String, Object> propertyMap = toMap(pojoValue);
        propertyMap.putAll(extraFields);
        return propertyMap;
    }
}
