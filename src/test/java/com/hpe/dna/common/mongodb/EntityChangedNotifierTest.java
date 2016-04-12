package com.hpe.dna.common.mongodb;

import com.hpe.dna.common.mongodb.notifier.EntityChangedNotifier;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

/**
 * @author chun-yang.wang@hpe.com
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class EntityChangedNotifierTest {
    private static final Logger logger = LoggerFactory.getLogger(EntityChangedNotifier.class);
    @Inject
    private EntityManager entityManager;
    @Inject
    private MongoManager mongoManager;

    @After
    public void clean() {
        mongoManager.dropCollection(DummyEntity.class);
    }

    @Test
    public void createEntity() {
        DummyEntity entity = new DummyEntity();
        entity.setStringProperty("str");
        entityManager.create(entity);
    }
}
