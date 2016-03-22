package com.hpe.dna.common.http;

/**
 * @author chun-yang.wang@hpe.com
 */
public class HttpClientResponse {

    private final int statusCode;
    private final String data;

    public HttpClientResponse(int statusCode, String data) {
        this.statusCode = statusCode;
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getData() {
        return data;
    }
}
