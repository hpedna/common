package com.hpe.dna.common.mongodb;

import com.google.common.base.CaseFormat;
import com.google.common.base.Strings;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Named;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * @author chun-yang.wang@hpe.com
 */
@Named
public class CollectionNameResolver {
    private static final Logger logger = getLogger(CollectionNameResolver.class);
    private LoadingCache<Class<?>, String> cachedCollectionNames;

    @PostConstruct
    public void _init() {
        cachedCollectionNames = CacheBuilder.newBuilder()
                .build(new CacheLoader<Class<?>, String>() {
                    @Override
                    public String load(Class<?> type) {
                        return doResolve(type);
                    }
                });
    }


    public String resolve(Class<?> type) {
        return cachedCollectionNames.getUnchecked(type);
    }

    private String doResolve(Class<?> type) {
        String collectionName;
        MongoCollectionAnnotation annotation = type.getAnnotation(MongoCollectionAnnotation.class);
        if (annotation == null || Strings.isNullOrEmpty(annotation.value())) {
            collectionName = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, type.getSimpleName());
        } else {
            collectionName = annotation.value().trim();
        }
        logger.info("Collection name {} was extracted from type {}", collectionName, type.getTypeName());
        return collectionName;
    }
}
