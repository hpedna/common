package com.hpe.dna.common.mongodb.notifier;

import com.hpe.dna.common.mongodb.Entity;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author chun-yang.wang@hpe.com
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EntityChangedListener {
    Class<? extends Entity>[] value();
}
