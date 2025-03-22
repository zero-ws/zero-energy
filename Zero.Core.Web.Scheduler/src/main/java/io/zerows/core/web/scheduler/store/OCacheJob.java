package io.zerows.core.web.scheduler.store;

import io.horizon.uca.cache.Cc;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.zdk.running.OCache;
import io.zerows.core.web.scheduler.atom.Mission;
import org.osgi.framework.Bundle;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lang : 2024-04-30
 */
public interface OCacheJob extends OCache<Set<Mission>> {
    Cc<String, OCacheJob> CC_SKELETON = Cc.open();

    static OCacheJob of(final Bundle bundle) {
        final String cacheKey = Ut.Bnd.keyCache(bundle, OCacheJobAmbiguity.class);
        return CC_SKELETON.pick(() -> new OCacheJobAmbiguity(bundle), cacheKey);
    }

    static OCacheJob of() {
        return of(null);
    }

    static Set<Mission> entireValue() {
        return CC_SKELETON.store().values().stream()
            .flatMap(cache -> cache.value().stream())
            .collect(Collectors.toSet());
    }
}
