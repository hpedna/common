package com.hpe.dna.common.mongodb;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * @author chun-yang.wang@hpe.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class MongoMapperTest {

    @Inject
    private MongoMapper mapper;

    @Test
    public void toDocument_NewEntity() {
        User user = new User();
        user.setEmail("chun-yang.wang@hpe.com");
        Document doc = mapper.toDocument(user);
        assertEquals("chun-yang.wang@hpe.com", doc.getString("email"));
        assertNull(doc.getObjectId("_id"));
    }

    @Test
    public void toDocument_NullEntity() {
        Document doc = mapper.toDocument(null);
        assertNull(doc);
    }

    @Test
    public void toDocument_InvalidObjectId() {
        User user = new User();
        user.setId("abc123");
        Document doc = mapper.toDocument(user);
        assertNull(doc.getObjectId("_id"));
    }

    @Test
    public void toDocument_ValidObjectId() {
        User user = new User();
        user.setId(ObjectId.get().toString());
        Document doc = mapper.toDocument(user);
        assertNotNull(doc.getObjectId("_id"));
        assertTrue(ObjectId.isValid(doc.getObjectId("_id").toString()));
    }


    @Test
    public void fromDocument() {
        Document doc = new Document("email", "chun-yang.wang@hpe.com").append("_id", ObjectId.get());
        User user = mapper.fromDocument(doc, User.class);
        assertEquals("chun-yang.wang@hpe.com", user.getEmail());
        assertNotNull(user.getId());
    }

    @Test
    public void toDocuments() {
        List<Entity> entities = new ArrayList<>();
        User u1 = new User();
        u1.setEmail("chun-yang.wang@hpe.com");
        entities.add(u1);

        User u2 = new User();
        u2.setEmail("chun-yang.wang2@hpe.com");

        entities.add(u2);
        List<Document> docs = mapper.toDocuments(entities);
        assertEquals(2, docs.size());
        assertEquals("chun-yang.wang@hpe.com", docs.get(0).getString("email"));
        assertEquals("chun-yang.wang2@hpe.com", docs.get(1).getString("email"));
    }

    @Test
    public void fromDocuments() {
        List<Document> docs = new ArrayList<>();
        Document doc1 = new Document("email", "chun-yang.wang@hpe.com").append("_id", ObjectId.get());
        docs.add(doc1);

        Document doc2 = new Document("email", "chun-yang.wang2@hpe.com").append("_id", ObjectId.get());
        docs.add(doc2);

        List<User> users = mapper.fromDocuments(docs, User.class);
        assertEquals(2, users.size());
        assertEquals("chun-yang.wang@hpe.com", users.get(0).getEmail());
        assertNotNull(users.get(0).getId());

        assertEquals("chun-yang.wang2@hpe.com", users.get(1).getEmail());
        assertNotNull(users.get(1).getId());
    }

    private class User extends Entity {
        private String id;
        private String email;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }
}