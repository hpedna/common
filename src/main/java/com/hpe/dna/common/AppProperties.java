package com.hpe.dna.common;

import com.google.common.base.MoreObjects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author chun-yang.wang@hpe.com
 */
@Configuration
@PropertySource("classpath:app.properties")
public class AppProperties {
    @Value("${mongo.connection.string}")
    private String mongoConnectionString;

    @Value("${mongo.db.name}")
    private String mongoDbName;

    public String getMongoConnectionString() {
        return mongoConnectionString;
    }

    public void setMongoConnectionString(String mongoConnectionString) {
        this.mongoConnectionString = mongoConnectionString;
    }

    public String getMongoDbName() {
        return mongoDbName;
    }

    public void setMongoDbName(String mongoDbName) {
        this.mongoDbName = mongoDbName;
    }

}
