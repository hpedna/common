package com.hpe.dna.common.mongodb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * @author chun-yang.wang@hpe.com
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:applicationContext.xml"})
public class CollectionNameResolverTest {

    @Inject
    private CollectionNameResolver resolver;

    @Test
    public void resolve() {
        assertEquals("entity_test", resolver.resolve(EntityAnnotationWithValue.class));
        assertEquals("entity_default", resolver.resolve(EntityDefault.class));
        assertEquals("default", resolver.resolve(Default.class));
        assertEquals("entity_default_default", resolver.resolve(EntityDefaultDefault.class));
    }

    @MongoCollectionAnnotation("entity_test")
    private class EntityAnnotationWithValue extends Entity {
    }

    @MongoCollectionAnnotation("")
    private class EntityDefault extends Entity {
    }

    private class Default extends Entity {
    }

    private class EntityDefaultDefault extends Entity {
    }
}