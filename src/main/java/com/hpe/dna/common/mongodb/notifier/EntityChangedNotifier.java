package com.hpe.dna.common.mongodb.notifier;

import com.hpe.dna.common.mongodb.Entity;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author chun-yang.wang@hpe.com
 */

@Named
public class EntityChangedNotifier {
    private Map<Class<?>, List<IEntityChangedListener>> entitiesListeners = new HashMap<>();

    private ExecutorService executorService;

    private boolean async = true;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @PreDestroy
    public void cleanUp() {
        executorService.shutdown();
    }

    public <T extends Entity> void fireEntityChanged(EntityChangedEvent event) {
        Class<? extends Entity> entityType = event.getEntityType();
        if (!entitiesListeners.containsKey(entityType))
            return;

        if (async) {
            executorService.submit((Runnable) () -> {
                doFire(event);
            });
        } else {
            doFire(event);
        }
    }

    private void doFire(EntityChangedEvent event) {
        for (IEntityChangedListener listener : entitiesListeners.get(event.getEntityType())) {
            listener.onEntityChanged(event);
        }
    }

    public void register(Class<?> entityType, IEntityChangedListener listener) {
        entitiesListeners.putIfAbsent(entityType, new ArrayList<>());
        entitiesListeners.get(entityType).add(listener);
    }

    // Only used for unit testing
    public void _disableAsynchronousMode() {
        this.async = false;
    }
}
