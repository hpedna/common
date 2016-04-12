package com.hpe.dna.common.jersey;

import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.google.common.base.Strings;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ParamConverter;
import javax.ws.rs.ext.ParamConverterProvider;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * @author chun-yang.wang@hpe.com
 */

@Provider
public class DateParamConverterProvider implements ParamConverterProvider {

    @Override
    public <T> ParamConverter<T> getConverter(Class<T> rawType,
                                              Type genericType,
                                              Annotation[] annotations) {
        if (rawType != Date.class) {
            return null;
        }
        return (ParamConverter<T>) new ParamConverter<Date>() {
            @Override
            public Date fromString(String value) {
                if (Strings.isNullOrEmpty(value))
                    return null;
                try {
                    return StdDateFormat.instance.parse(value);
                } catch (Exception e) {
                    throw new WebApplicationException("Bad formatted date: " + value, 400);
                }
            }

            @Override
            public String toString(Date date) {
                return StdDateFormat.instance.format(date);
            }
        };
    }
}
