package com.hpe.dna.common.jersey;

import org.junit.Test;

import java.text.ParseException;
import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * @author chun-yang.wang@hpe.com
 */
public class DateParamConverterProviderTest {

    @Test
    public void test() throws ParseException {
        DateParamConverterProvider provider = new DateParamConverterProvider();
        Date date = provider.getConverter(Date.class, null, null).fromString("2016-02-11T03:09:26+0000");
        assertNotNull(date);

        Date date2 = provider.getConverter(Date.class, null, null).fromString("2016-02-11T03:09:26+00:00");
        assertNotNull(date2);

        Date date3 = provider.getConverter(Date.class, null, null).fromString("2016-02-11T03:09:26Z");
        assertNotNull(date3);
    }
}