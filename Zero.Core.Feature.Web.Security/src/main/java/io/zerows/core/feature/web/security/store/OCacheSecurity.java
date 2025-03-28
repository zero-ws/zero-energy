package io.zerows.core.feature.web.security.store;

import io.horizon.uca.cache.Cc;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.zdk.running.OCache;
import io.zerows.core.security.atom.Aegis;
import org.osgi.framework.Bundle;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author lang : 2024-04-22
 */
public interface OCacheSecurity extends OCache<Set<Aegis>> {
    Cc<String, OCacheSecurity> CC_SKELETON = Cc.open();

    static OCacheSecurity of() {
        return of(null);
    }

    static OCacheSecurity of(final Bundle bundle) {
        final String cacheKey = Ut.Bnd.keyCache(bundle, OCacheSecurityAmbiguity.class);
        return CC_SKELETON.pick(() -> new OCacheSecurityAmbiguity(bundle), cacheKey);
    }

    static ConcurrentMap<String, Set<Aegis>> entireWall() {
        final ConcurrentMap<String, Set<Aegis>> walls = new ConcurrentHashMap<>();
        CC_SKELETON.store().values().forEach(self -> walls.putAll(self.valueWall()));
        return walls;
    }

    OCacheSecurity remove(Aegis wall);

    OCacheSecurity add(Aegis wall);

    ConcurrentMap<String, Set<Aegis>> valueWall();
}
