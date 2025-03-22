package io.zerows.core.metadata.cache;

import io.horizon.uca.cache.Cc;
import io.zerows.core.metadata.uca.logging.OLog;

/**
 * @author lang : 2024-04-17
 */
interface CStoreTrack {

    Cc<String, OLog> CC_LOG = Cc.open();
}
