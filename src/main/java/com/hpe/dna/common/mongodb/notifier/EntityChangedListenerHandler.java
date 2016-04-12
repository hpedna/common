package com.hpe.dna.common.mongodb.notifier;

import com.hpe.dna.common.mongodb.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;

/**
 * @author chun-yang.wang@hpe.com
 */
@Named
public class EntityChangedListenerHandler {
    private static final Logger logger = LoggerFactory.getLogger(EntityChangedListenerHandler.class);

    @Inject
    private ApplicationContext applicationContext;

    @Inject
    private EntityChangedNotifier eventNotifier;

    @PostConstruct
    public void init() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(EntityChangedListener.class);
        for (Object bean : beans.values()) {
            EntityChangedListener annotation = AnnotationUtils.getAnnotation(bean.getClass(), EntityChangedListener.class);
            if (bean instanceof IEntityChangedListener) {
                for (Class<? extends Entity> entityType : annotation.value()) {
                    logger.info("Register EntityChangedListener: {} listen on {}", bean.getClass().getName(), entityType.getName());
                    eventNotifier.register(entityType, (IEntityChangedListener) bean);
                }
            }
        }
    }
}
