package com.hpe.dna.common.mongodb;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hpe.dna.common.AppRuntimeException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * The mapper is mapping between POJO and MongoDB Document.
 * It is using Jackson as internal implementation to map between POJO and json string.
 *
 * @author chun-yang.wang@hpe.com
 */
@Named
public class MongoMapper {
    private static final Logger logger = getLogger(MongoMapper.class);
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(Date.class, new DateSerializer());
        module.addSerializer(byte[].class, new BinarySerializer());
        module.addDeserializer(Date.class, new DateDeserializer());
        module.addDeserializer(byte[].class, new BinaryDeserializer());
        objectMapper.registerModule(module);
    }

    /**
     * Map to Mongo's Document from Entity
     *
     * @param entity the POJO entity
     * @return Mongo's Document if input parameter entity is not null. Otherwise returns null.
     */
    public Document toDocument(Entity entity) {
        if (entity == null) {
            return null;
        }

        try {
            String json = objectMapper.writeValueAsString(entity);
            Document doc = Document.parse(json);
            // Map "pojo.id" filed to document identifier ("_id").
            doc.remove("id");
            String id = entity.getId();
            if (id != null && ObjectId.isValid(id)) {
                doc.append("_id", new ObjectId(id));
            }
            return doc;
        } catch (JsonProcessingException e) {
            throw new AppRuntimeException("Failed to map from entity to document", e);
        }

    }

    /**
     * Map to Entity from Mongo's Document
     *
     * @param document   the Mongo's document
     * @param entityType the type of entity
     * @return The entity mapped from document
     */
    public <T extends Entity> T fromDocument(Document document, Class<T> entityType) {
        if (document == null)
            return null;

        try {
            String json = document.toJson();
            T entity = objectMapper.readValue(json, entityType);
            entity.setId(document.getObjectId("_id").toString());
            return entity;
        } catch (IOException e) {
            throw new AppRuntimeException("Failed to map from document to entity", e);
        }
    }

    public <T extends Entity> List<Document> toDocuments(List<T> entities) {
        List<Document> docs = new ArrayList<>();
        if (entities == null) {
            return docs;
        }

        docs.addAll(entities.stream().map(this::toDocument).collect(Collectors.toList()));
        return docs;
    }

    public <T extends Entity> List<T> fromDocuments(List<Document> docs, Class<T> entityType) {
        List<T> entities = new ArrayList<>();
        if (docs == null || docs.size() == 0)
            return entities;

        for (Document doc : docs) {
            entities.add(fromDocument(doc, entityType));
        }
        return entities;
    }
}
