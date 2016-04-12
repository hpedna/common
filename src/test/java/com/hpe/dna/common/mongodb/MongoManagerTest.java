package com.hpe.dna.common.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

/**
 * @author chun-yang.wang@hpe.com
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class MongoManagerTest {

    @Inject
    private MongoManager mongoManager;

    @Test
    public void testDb() {
        MongoClient client = mongoManager.getMongoClient();
        assertNotNull(client);

        MongoDatabase db = mongoManager.getDb();
        assertNotNull(db);
    }

    @Test
    public void dropCollection(){
        mongoManager.dropCollection("abc");
    }
}