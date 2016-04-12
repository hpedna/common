package com.hpe.dna.common.mongodb.notifier;

import com.hpe.dna.common.mongodb.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chun-yang.wang@hpe.com
 */
public class EntityChangedEvent {
    public enum EEventType {CREATED, BULK_CREATED, DELETED, UPDATED}

    private EEventType eventType;
    private Class<? extends Entity> entityType;
    private List<String> entityIds;
    private List<? extends Entity> entitiesDeleted;

    public EntityChangedEvent(EEventType eventType, Class<? extends Entity> entityType, List<String> entityIds) {
        this.entityIds = entityIds;
        this.entityType = entityType;
        this.eventType = eventType;
        this.entitiesDeleted = new ArrayList<>();
    }

    public void setEntityType(Class<? extends Entity> entityType) {
        this.entityType = entityType;
    }

    public Class<? extends Entity> getEntityType() {
        return entityType;
    }

    public void setEntityIds(List<String> entityIds) {
        this.entityIds = entityIds;
    }

    public List<String> getEntityIds() {
        return entityIds;
    }

    public EEventType getEventType() {
        return eventType;
    }

    public void setEventType(EEventType eventType) {
        this.eventType = eventType;
    }

    public List<? extends Entity> getEntitiesDeleted() {
        return entitiesDeleted;
    }

    public void setEntitiesDeleted(List<? extends Entity> entitiesDeleted) {
        this.entitiesDeleted = entitiesDeleted;
    }

    @Override
    public String toString() {
        return "EntityChangedEvent{" +
                "eventType=" + eventType +
                ", entityType=" + entityType +
                ", entityIds=" + entityIds +
                ", entitiesDeleted=" + entitiesDeleted +
                '}';
    }
}
