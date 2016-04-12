package com.hpe.dna.common.mongodb;

import com.mongodb.Block;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.conversions.Bson;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;

/**
 * @author chun-yang.wang@hpe.com
 */
@Named
public class MongoAggregator {

    @Inject
    private MongoManager mongoManager;

    public long count(Class<?> type) {
        MongoCollection<Document> coll = mongoManager.getCollection(type);
        return coll.count();
    }

    public long count(Class<?> type, Bson filter) {
        MongoCollection<Document> coll = mongoManager.getCollection(type);
        return coll.count(filter);
    }

    public void aggregate(Class<?> type,
                          Map<String, Object> match,
                          Map<String, Object> group,
                          Block<? super Document> block) {
        Document matchFields = new Document();
        matchFields.putAll(match);

        Document groupFields = new Document();
        groupFields.putAll(group);

        aggregate(type, new Document("$match", matchFields), new Document("$group", groupFields), block);
    }

    public void aggregate(Class<?> type, Document match, Document group, Block<? super Document> block) {
        MongoCollection<Document> coll = mongoManager.getCollection(type);
        List<Document> pipeline = asList(match, group);
        AggregateIterable<Document> it = coll.aggregate(pipeline);
        it.forEach(block);
    }

    public AggregateIterable<Document> aggregate(Class<?> entityType, Bson... pipeline) {
        MongoCollection<Document> coll = mongoManager.getCollection(entityType);
        return coll.aggregate(Arrays.asList(pipeline));
    }
}
