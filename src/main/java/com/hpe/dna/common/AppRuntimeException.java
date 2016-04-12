package com.hpe.dna.common;

import org.springframework.core.NestedRuntimeException;

/**
 * @author chun-yang.wang@hpe.com
 */
public class AppRuntimeException extends NestedRuntimeException {
    private static final long serialVersionUID = 2810764658532992659L;

    public AppRuntimeException(String msg) {
        super(msg);
    }

    public AppRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
}
