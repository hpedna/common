package com.hpe.dna.common.mongodb;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Date;

/**
 * Serialises java.util.Date as BSON date.
 * Date => { "$date": 1393804800000}
 *
 * @author chun-yang.wang@hpe.com
 */
public class DateSerializer extends JsonSerializer<Date> {
    @Override
    public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("$date", value.getTime());
        gen.writeEndObject();
    }
}
