package com.hpe.dna.common.mongodb;

import com.google.common.base.Strings;
import com.hpe.dna.common.mongodb.notifier.EntityChangedEvent;
import com.hpe.dna.common.mongodb.notifier.EntityChangedNotifier;
import com.hpe.dna.common.security.UserIdHolder;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.collect.ImmutableList.of;
import static com.hpe.dna.common.mongodb.notifier.EntityChangedEvent.EEventType.*;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * @author chun-yang.wang@hpe.com
 */
@Named
public class EntityManager {
    private static final Logger logger = LoggerFactory.getLogger(EntityManager.class);

    @Inject
    private MongoManager mongoManager;

    @Inject
    private MongoMapper mapper;

    @Inject
    private CollectionNameResolver nameResolver;

    @Inject
    private EntityChangedNotifier entityChangedNotifier;

    /**
     * Create an Entity in MongoDB.
     *
     * @param entity the instance of entity
     * @param <T>    the type of entity
     * @return the created entity
     */
    public <T extends Entity> T create(T entity) {
        String collName = nameResolver.resolve(entity.getClass());
        entity.setCreatedOn(new Date());
        entity.setCreatedBy(UserIdHolder.get());
        entity.setModifiedOn(new Date());
        entity.setModifiedBy(UserIdHolder.get());

        Document doc = mapper.toDocument(entity);
        MongoCollection<Document> coll = mongoManager.getCollection(collName);
        coll.insertOne(doc);
        entity.setId(doc.getObjectId("_id").toString());
        logger.debug("Document {} was created in collection {}", entity.getId(), collName);
        entityChangedNotifier.fireEntityChanged(new EntityChangedEvent(CREATED, entity.getClass(), of(entity.getId())));
        return entity;
    }

    /**
     * Create Entities in MongoDB using bulkInsert
     *
     * @param entities   the entity list
     * @param entityType the type of entity
     */
    public <T extends Entity> boolean create(Class<?> entityType, List<T> entities) {
        return create(nameResolver.resolve(entityType), entities);
    }

    public <T extends Entity> boolean create(String collName, List<T> entities) {
        if (entities == null || entities.size() == 0) {
            return false;
        }

        Date now = new Date();
        entities.stream().filter(entity -> entity.getCreatedOn() == null).forEach(entity -> {
            entity.setCreatedOn(now);
            entity.setCreatedBy(UserIdHolder.get());
            entity.setModifiedOn(new Date());
            entity.setModifiedBy(UserIdHolder.get());
        });
        MongoCollection<Document> coll = mongoManager.getCollection(collName);
        List<Document> docs = mapper.toDocuments(entities);
        coll.insertMany(docs);
        logger.debug("{} documents were created in collection {}", docs.size(), collName);
        List<String> ids = docs.stream().map(doc -> doc.getObjectId("_id").toString()).collect(Collectors.toList());
        entityChangedNotifier.fireEntityChanged(new EntityChangedEvent(BULK_CREATED, entities.get(0).getClass(), ids));
        return true;
    }

    /**
     * Delete an entity by Id.
     *
     * @param type the type of entity
     * @param id   the id of entity
     * @return EMongoResult of delete operation
     */
    public <T extends Entity> EMongoResult delete(Class<T> type, String id) {
        MongoCollection<Document> collection = mongoManager.getCollection(type);

        T entityToDelete = getEntity(type, id);
        if (entityToDelete == null) {
            return EMongoResult.ENTITY_NOT_FOUND;
        }

        DeleteResult result = collection.deleteOne(eq("_id", new ObjectId(id)));
        logger.debug("Document {} was deleted from {}", id, collection.getNamespace().getCollectionName());
        if (result.getDeletedCount() > 0) {
            EntityChangedEvent event = new EntityChangedEvent(DELETED, type, of(id));
            event.setEntitiesDeleted(of(entityToDelete));
            entityChangedNotifier.fireEntityChanged(event);
        }
        return EMongoResult.of(result);
    }

    public <T extends Entity> EMongoResult deleteBy(Class<T> type, Bson filter) {
        MongoCollection<Document> collection = mongoManager.getCollection(type);

        List<T> entitiesToDelete = getEntitiesBy(type, filter);

        DeleteResult deleteResult = collection.deleteMany(filter);
        logger.debug("{} documents are deleted from {}",
                deleteResult.getDeletedCount(), collection.getNamespace().getCollectionName());

        if (deleteResult.getDeletedCount() > 0) {
            EntityChangedEvent event = new EntityChangedEvent(DELETED, type, new ArrayList<>());
            event.setEntitiesDeleted(entitiesToDelete);
            entityChangedNotifier.fireEntityChanged(event);
        }
        return EMongoResult.of(deleteResult);
    }

    /**
     * To replace the entire content of a document except for the _id field.
     *
     * @param entity
     * @return
     */
    public <T extends Entity> EMongoResult replace(T entity) {
        MongoCollection<Document> collection = mongoManager.getCollection(entity.getClass());
        entity.setModifiedOn(new Date());
        entity.setModifiedBy(UserIdHolder.get());
        Document filter = new Document("_id", new ObjectId(entity.getId()));
        UpdateResult result = collection.replaceOne(filter, mapper.toDocument(entity));
        logger.debug("Replaced document {} in {}", entity.getId(), collection.getNamespace().getCollectionName());
        EntityChangedEvent event = new EntityChangedEvent(UPDATED, entity.getClass(), of(entity.getId()));
        entityChangedNotifier.fireEntityChanged(event);
        return EMongoResult.of(result);
    }

    /**
     * Update Specific Fields in a Document
     *
     * @param entity
     * @return
     */
    public <T extends Entity> EMongoResult update(T entity) {
        MongoCollection<Document> collection = mongoManager.getCollection(entity.getClass());
        entity.setModifiedOn(new Date());
        entity.setModifiedBy(UserIdHolder.get());
        Document update = mapper.toDocument(entity);
        Document filter = new Document("_id", new ObjectId(entity.getId()));
        UpdateResult result = collection.updateOne(filter, new Document("$set", update));
        logger.debug("Updated document {} in {}", entity.getId(), collection.getNamespace().getCollectionName());
        entityChangedNotifier.fireEntityChanged(new EntityChangedEvent(UPDATED, entity.getClass(), of(entity.getId())));
        return EMongoResult.of(result);
    }


    public <T extends Entity> EMongoResult update(Class<T> type, String id, Map<String, Object> fields) {
        MongoCollection<Document> collection = mongoManager.getCollection(type);
        Document update = new Document("modified", new Date()).append("modifiedBy", UserIdHolder.get());
        for (String field : fields.keySet()) {
            update.append(field, fields.get(field));
        }
        Document filter = new Document("_id", new ObjectId(id));
        UpdateResult result = collection.updateOne(filter, new Document("$set", update));
        logger.debug("Updated document {} in {}", id, collection.getNamespace().getCollectionName());
        entityChangedNotifier.fireEntityChanged(new EntityChangedEvent(UPDATED, type, of(id)));
        return EMongoResult.of(result);
    }

    public <T extends Entity> T getEntity(Class<T> type, String id) {
        if (Strings.isNullOrEmpty(id) || !ObjectId.isValid(id)) return null;
        MongoCollection<Document> coll = mongoManager.getCollection(type);
        Document doc = coll.find(eq("_id", new ObjectId(id))).first();
        if (doc != null) {
            return mapper.fromDocument(doc, type);
        }
        logger.debug("Found document {} in {}", id, coll.getNamespace().getCollectionName());
        return null;
    }
    public <T extends Entity> List<T> getEntities(Class<T> type) {
        return getEntities(null, type);
    }

    public <T extends Entity> List<T> getEntities(String collectionName, Class<T> type) {
        return getEntities(collectionName, type, null);
    }

    public <T extends Entity> List<T> getEntities(Class<T> type, Bson sortSpecification) {
        return getEntities(null, type, sortSpecification);
    }

    public <T extends Entity> List<T> getEntities(String collectionName, Class<T> type, Bson sortSpecification) {
        MongoCollection<Document> collection = collectionName == null ? mongoManager.getCollection(type) : mongoManager.getCollection(collectionName);

        FindIterable<Document> findIterable = collection.find();
        if (sortSpecification != null) {
            findIterable.sort(sortSpecification);
        }

        List<T> entities = new ArrayList<>();
        try (MongoCursor<Document> cursor = findIterable.iterator()) {
            while (cursor.hasNext()) {
                T entity = mapper.fromDocument(cursor.next(), type);
                entities.add(entity);
            }
        }
        logger.debug("Found {} entities in {}", entities.size(), collection.getNamespace().getCollectionName());
        return entities;
    }

    public <T extends Entity> T getEntityBy(Class<T> type, Bson filters) {
        return getEntityBy(null, type, filters);
    }

    public <T extends Entity> T getEntityBy(String collectionName, Class<T> type, Bson filters) {
        MongoCollection<Document> collection = collectionName == null ? mongoManager.getCollection(type) : mongoManager.getCollection(collectionName);
        Document doc = collection.find(filters).first();
        if (doc != null) {
            return mapper.fromDocument(doc, type);
        }
        logger.debug("Found document {} in {}", filters, collection.getNamespace().getCollectionName());
        return null;
    }

    public <T extends Entity> List<T> getEntitiesBy(Class<T> type, Bson filters) {
        return getEntitiesBy(null, type, filters);
    }

    public <T extends Entity> List<T> getEntitiesBy(String collectionName, Class<T> type, Bson filters) {
        MongoCollection<Document> collection = collectionName == null ? mongoManager.getCollection(type) : mongoManager.getCollection(collectionName);
        FindIterable<Document> findIterable = collection.find(filters);
        List<T> entities = new ArrayList<>();
        try (MongoCursor<Document> cursor = findIterable.iterator()) {
            while (cursor.hasNext()) {
                T entity = mapper.fromDocument(cursor.next(), type);
                entities.add(entity);
            }
        }
        logger.debug("Found {} entities in {}", entities.size(), collection.getNamespace().getCollectionName());
        return entities;
    }

    public <T extends Entity> List<T> getEntitiesBy(Class<T> type, Bson filters, Bson sortSpecification) {
        return getEntitiesBy(null, type, filters, sortSpecification);
    }

    public <T extends Entity> List<T> getEntitiesBy(String collectionName, Class<T> type, Bson filters, Bson sortSpecification) {
        MongoCollection<Document> collection = collectionName == null ? mongoManager.getCollection(type) : mongoManager.getCollection(collectionName);

        FindIterable<Document> findIterable = collection.find(filters);

        if (sortSpecification != null) {
            findIterable.sort(sortSpecification);
        }

        List<T> entities = new ArrayList<>();
        try (MongoCursor<Document> cursor = findIterable.iterator()) {
            while (cursor.hasNext()) {
                T entity = mapper.fromDocument(cursor.next(), type);
                entities.add(entity);
            }
        }
        logger.debug("Found {} entities in {} ", entities.size(), collection.getNamespace().getCollectionName());
        return entities;
    }


    public <T extends Entity> List<T> getEntitiesPagination(Class<T> type, Integer pageSize, Integer pageNumber) {
        MongoCollection<Document> collection = mongoManager.getCollection(type);
        int skip = 0;
        if (pageNumber > 0) {
            skip = (pageNumber - 1) * pageSize;
        }
        List<T> entities = new ArrayList<>();
        try (MongoCursor<Document> cursor = collection.find().skip(skip).limit(pageSize).iterator()) {
            while (cursor.hasNext()) {
                T entity = mapper.fromDocument(cursor.next(), type);
                entities.add(entity);
            }
        }
        logger.debug("Found {} entities in {}", entities.size(), collection.getNamespace().getCollectionName());
        return entities;
    }
    /**
     * Counts the number of documents in the collection.
     *
     * @param type Type of an entity
     * @return the number of documents in the collection
     */
    public long count(Class<?> type) {
        return mongoManager.getCollection(type).count();
    }

    public long count(Class<?> type, Map<String, Object> filters) {
        List<Bson> filterList = new ArrayList<>();
        for (String key : filters.keySet()) {
            Object value = filters.get(key);
            filterList.add(eq(key, value));
        }
        return mongoManager.getCollection(type).count(and(filterList));
    }
}
