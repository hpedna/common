package com.hpe.dna.common.mongodb;

import com.google.common.collect.ImmutableList;
import org.bson.types.ObjectId;
import org.glassfish.jersey.internal.util.Base64;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.mongodb.client.model.Filters.*;
import static org.junit.Assert.*;

/**
 * @author chun-yang.wang@hpe.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class EntityManagerTest {
    @Inject
    private EntityManager entityManager;
    @Inject
    private MongoManager mongoManager;

    @After
    public void after() {
        mongoManager.dropCollection(DummyEntity.class);
    }

    @Test
    public void testObjectId() {
        DummyEntity entity = new DummyEntity();
        entityManager.create(entity);
        assertNotNull(entity.getId());
        assertTrue(ObjectId.isValid(entity.getId()));
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
    }

    @Test
    public void testDate() {
        DummyEntity entity = new DummyEntity();
        Date now = new Date();
        entity.setDateProperty(now);
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertEquals(now.getTime(), foundEntity.getDateProperty().getTime());
    }

    @Test
    public void testDouble() {
        DummyEntity entity = new DummyEntity();
        entity.setDoubleProperty(0.785);
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());

        assertNotNull(foundEntity);
        assertEquals(Double.valueOf(0.785), foundEntity.getDoubleProperty());
    }

    @Test
    public void testLong() {
        DummyEntity entity = new DummyEntity();
        entity.setLongProperty(80L);
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertEquals(Long.valueOf(80), foundEntity.getLongProperty());
    }

    @Test
    public void testBoolean() {
        DummyEntity entity = new DummyEntity();
        entity.setBooleanProperty(true);
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertTrue(foundEntity.getBooleanProperty());

        entity = new DummyEntity();
        entity.setBooleanProperty(false);
        entityManager.create(entity);
        assertNotNull(entity.getId());
        foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertFalse(foundEntity.getBooleanProperty());
    }

    @Test
    public void testInteger() {
        DummyEntity entity = new DummyEntity();
        entity.setIntegerProperty(40);
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertEquals(Integer.valueOf(40), foundEntity.getIntegerProperty());
    }

    @Test
    public void testString() {
        DummyEntity entity = new DummyEntity();
        entity.setStringProperty("str");
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertEquals("str", foundEntity.getStringProperty());
    }

    @Test
    public void testList() {
        DummyEntity entity = new DummyEntity();
        String[] elements = {"e1", "e2", "e3"};
        entity.setListProperty(Arrays.asList(elements));
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertArrayEquals(elements, foundEntity.getListProperty().toArray());
    }

    @Test
    public void testBinary() {
        DummyEntity entity = new DummyEntity();
        byte[] bytes = {'1', '2', '3'};
        entity.setBinaryProperty(bytes);
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertArrayEquals(bytes, foundEntity.getBinaryProperty());
    }

    @Test
    public void testBinaryBase64() {
        DummyEntity entity = new DummyEntity();
        byte[] bytes = {'1', '2', '3'};
        byte[] encodedBytes = Base64.encode(bytes);
        entity.setBinaryProperty(encodedBytes);
        entityManager.create(entity);
        assertNotNull(entity.getId());
        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, entity.getId());
        assertNotNull(foundEntity);
        assertArrayEquals(encodedBytes, foundEntity.getBinaryProperty());
    }

    @Test
    public void update() {
        DummyEntity entity = new DummyEntity();
        entity.setIntegerProperty(20);
        entity.setStringProperty("str");
        DummyEntity createdEntity = entityManager.create(entity);

        DummyEntity toUpdateEntity = new DummyEntity();
        toUpdateEntity.setId(createdEntity.getId());
        toUpdateEntity.setStringProperty("str2");
        EMongoResult result = entityManager.update(toUpdateEntity);
        assertEquals(EMongoResult.SUCCESS, result);

        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, createdEntity.getId());
        assertNotNull(foundEntity.getModifiedOn());
        assertNull(foundEntity.getIntegerProperty());
        assertEquals("str2", foundEntity.getStringProperty());
    }

    @Test
    public void replace() {
        DummyEntity entity = new DummyEntity();
        entity.setIntegerProperty(20);
        entity.setStringProperty("str");
        DummyEntity createdEntity = entityManager.create(entity);

        DummyEntity toReplaceEntity = new DummyEntity();
        toReplaceEntity.setId(createdEntity.getId());
        toReplaceEntity.setStringProperty("str2");
        EMongoResult result = entityManager.replace(toReplaceEntity);
        assertEquals(EMongoResult.SUCCESS, result);

        DummyEntity foundEntity = entityManager.getEntity(DummyEntity.class, createdEntity.getId());
        assertNotNull(foundEntity.getModifiedOn());
        assertNull(foundEntity.getIntegerProperty());
        assertEquals("str2", foundEntity.getStringProperty());
    }

    @Test
    public void getEntitiesByIn() {
        DummyEntity entity1_1 = new DummyEntity();
        entity1_1.setIntegerProperty(1);
        entity1_1.setStringProperty("id1");
        entity1_1.setBooleanProperty(true);

        DummyEntity entity1_2 = new DummyEntity();
        entity1_2.setIntegerProperty(2);
        entity1_2.setStringProperty("id1");
        entity1_2.setBooleanProperty(true);

        DummyEntity entity2 = new DummyEntity();
        entity2.setIntegerProperty(3);
        entity2.setStringProperty("id2");
        entity2.setBooleanProperty(false);

        DummyEntity entity3 = new DummyEntity();
        entity3.setIntegerProperty(3);
        entity3.setStringProperty("id3");
        entity3.setBooleanProperty(false);

        DummyEntity dummyEntity1_1 = entityManager.create(entity1_1);
        DummyEntity dummyEntity1_2 = entityManager.create(entity1_2);
        DummyEntity dummyEntity2 = entityManager.create(entity2);
        DummyEntity dummyEntity3 = entityManager.create(entity3);

        List<DummyEntity> list = entityManager.getEntitiesBy(DummyEntity.class, in("stringProperty", ImmutableList.of("id1", "id3")));
        assertEquals(3, list.size());
        DummyEntity dummyEntity = list.get(2);
        assertNotNull(dummyEntity);
        assertEquals("id3", dummyEntity.getStringProperty());

        list = entityManager.getEntitiesBy(DummyEntity.class, and(eq("integerProperty", 3), in("stringProperty", ImmutableList.of("id1", "id3"))));
        assertEquals(1, list.size());

        entityManager.delete(DummyEntity.class, dummyEntity1_1.getId());
        entityManager.delete(DummyEntity.class, dummyEntity1_2.getId());
        entityManager.delete(DummyEntity.class, dummyEntity2.getId());
        entityManager.delete(DummyEntity.class, dummyEntity3.getId());
    }
}