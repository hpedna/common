package com.hpe.dna.common.mongodb;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Base64;

/**
 * Deserialize the BSON binary data to bytes.
 * <pre>
 *      {@code
 *       { "$binary": "<bindata>", "$type": "<t>" } => bytes[]
 *      }
 * </pre>
 * The bindata is the base64 representation of a binary string.
 *
 * @author chun-yang.wang@hpe.com
 */
public class BinaryDeserializer extends JsonDeserializer<byte[]> {
    @Override
    public byte[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String fieldName = null;
        String bindata = null;
        String t = null;
        while (p.hasCurrentToken()) {
            JsonToken token = p.nextToken();
            if (token == JsonToken.FIELD_NAME) {
                fieldName = p.getCurrentName();
            } else if (token == JsonToken.VALUE_STRING) {
                if ("$binary".equals(fieldName)) {
                    bindata = p.getValueAsString();
                } else if ("$type".equals(fieldName)) {
                    t = p.getValueAsString();
                } else {
                    throw new JsonParseException(p, "Unexpected field name", p.getTokenLocation());
                }
            } else if (token == JsonToken.END_OBJECT) {
                break;
            }
        }
        if (bindata != null && t != null) {
            return Base64.getDecoder().decode(bindata.getBytes());
        }
        return null;
    }
}
