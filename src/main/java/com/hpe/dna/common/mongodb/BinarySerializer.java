package com.hpe.dna.common.mongodb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.bson.BsonBinarySubType;

import java.io.IOException;

/**
 * Serializes the bytes to BSON binary data.
 * <pre>
 *     {@code
 *      bytes[] => { "$binary": "<bindata>", "$type": "<t>" }
 *      }
 * </pre>
 * bindata is the base64 representation of a binary string.
 * And Jackson also converts byte[] into Base64-encoded binary data automatically.
 *
 * @author chun-yang.wang@hpe.com
 */
public class BinarySerializer extends JsonSerializer<byte[]> {
    @Override
    public void serialize(byte[] value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeBinaryField("$binary", value);
        gen.writeStringField("$type", String.valueOf(BsonBinarySubType.BINARY.getValue()));
        gen.writeEndObject();
    }
}
