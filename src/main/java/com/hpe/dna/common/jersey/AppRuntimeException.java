package com.hpe.dna.common.jersey;

import org.springframework.core.NestedRuntimeException;

/**
 * @author chun-yang.wang@hpe.com
 */
public class AppRuntimeException extends NestedRuntimeException {
    public AppRuntimeException(String msg) {
        super(msg);
    }

    public AppRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
