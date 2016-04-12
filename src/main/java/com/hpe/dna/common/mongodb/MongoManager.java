package com.hpe.dna.common.mongodb;

import com.hpe.dna.common.AppProperties;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author chun-yang.wang@hpe.com
 */
@Named
public class MongoManager {
    private static final Logger logger = LoggerFactory.getLogger(EntityManager.class);

    @Inject
    private AppProperties appProperties;

    private MongoDatabase db;
    private MongoClient mongoClient;

    @Inject
    private CollectionNameResolver collectionNameResolver;

    @PostConstruct
    public void init() {
        String connectionString = appProperties.getMongoConnectionString();
        logger.info("MongoDB connection string: {}", connectionString);
        mongoClient = new MongoClient(new MongoClientURI(connectionString));

        String dbName = appProperties.getMongoDbName();
        logger.info("MongoDB database name: {}", dbName);
        db = mongoClient.getDatabase(dbName);
    }

    @PreDestroy
    public void shutdown() {
        mongoClient.close();
    }

    public MongoCollection<Document> getCollection(Class<?> entityType) {
        return getCollection(collectionNameResolver.resolve(entityType));
    }

    public MongoCollection<Document> getCollection(String collName) {
        return db.getCollection(collName);
    }

    public void dropCollection(Class<?> entityType) {
        String name = collectionNameResolver.resolve(entityType);
        dropCollection(name);
    }
    public void dropCollection(String collName) {
        MongoCollection<Document> collection = getCollection(collName);
        collection.drop();
    }

    public MongoDatabase getDb() {
        return db;
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }
}
