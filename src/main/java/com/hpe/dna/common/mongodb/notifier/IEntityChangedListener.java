package com.hpe.dna.common.mongodb.notifier;

/**
 * @author chun-yang.wang@hpe.com
 */
public interface IEntityChangedListener {
    void onEntityChanged(EntityChangedEvent event);
}
