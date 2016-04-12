package com.hpe.dna.common.mongodb;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * Deserialize BSON date to java.util.Date.
 * {@code
 * { "$date": 1393804800000} => Date
 * }
 *
 * @author chun-yang.wang@hpe.com
 */
public class DateDeserializer extends JsonDeserializer<Date> {
    @Override
    public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String fieldName = null;
        Long date = null;
        while (p.hasCurrentToken()) {
            JsonToken token = p.nextToken();
            if (token == JsonToken.FIELD_NAME) {
                fieldName = p.getCurrentName();
            } else if (token == JsonToken.VALUE_NUMBER_INT) {
                if ("$date".equals(fieldName)) {
                    date = p.getValueAsLong();
                } else {
                    throw new JsonParseException(p, "Unexpected field name", p.getTokenLocation());
                }
            } else if (token == JsonToken.END_OBJECT) {
                break;
            }
        }
        if (date != null) {
            return new Date(date);
        }
        return null;
    }
}
