package io.zerows.core.web.container.store.under;

import io.horizon.uca.cache.Cc;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.zdk.running.OCache;
import io.zerows.core.web.model.atom.running.RunRoute;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-05-03
 */
public interface StoreRouter extends OCache<RunRoute> {

    Cc<String, StoreRouter> CC_SKELETON = Cc.openThread();

    static StoreRouter of(final Bundle bundle) {
        final String cacheKey = Ut.Bnd.keyCache(bundle, StoreRouterAmbiguity.class);
        return CC_SKELETON.pick(() -> new StoreRouterAmbiguity(bundle), cacheKey);
    }

    StoreRouter addCurrent(RunRoute runRoute);
}
