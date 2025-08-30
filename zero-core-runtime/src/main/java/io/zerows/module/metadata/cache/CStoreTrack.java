package io.zerows.module.metadata.cache;

import io.zerows.core.uca.cache.Cc;
import io.zerows.module.metadata.uca.logging.OLog;

/**
 * @author lang : 2024-04-17
 */
interface CStoreTrack {

    Cc<String, OLog> CC_LOG = Cc.open();
}
