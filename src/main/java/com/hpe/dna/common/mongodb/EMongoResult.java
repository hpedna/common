package com.hpe.dna.common.mongodb;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

/**
 * @author chun-yang.wang@hpe.com
 */
public enum EMongoResult {
    CREATED_FAILED, ENTITY_NOT_FOUND, UPDATE_FAILED, DELETE_FAILED, SUCCESS;

    public static EMongoResult of(UpdateResult updateResult) {
        if (updateResult.getMatchedCount() == 0)
            return ENTITY_NOT_FOUND;
        if (updateResult.getModifiedCount() >= 0)
            return SUCCESS;
        return UPDATE_FAILED;
    }

    public static EMongoResult of(DeleteResult deleteResult) {
        if (deleteResult.getDeletedCount() > 0)
            return SUCCESS;
        return DELETE_FAILED;
    }
}
