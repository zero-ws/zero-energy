package io.zerows.core.web.container.store.under;

import io.horizon.exception.web._501NotSupportException;
import io.vertx.core.Vertx;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.atom.MultiKeyMap;
import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import io.zerows.core.web.model.atom.running.RunVertx;
import org.osgi.framework.Bundle;

import java.util.Objects;
import java.util.Set;

/**
 * @author lang : 2024-04-30
 */
class StoreVertxAmbiguity extends AbstractAmbiguity implements StoreVertx {

    private static final MultiKeyMap<RunVertx> RUNNING = new MultiKeyMap<>();

    StoreVertxAmbiguity(final Bundle bundle) {
        super(bundle);
    }

    @Override
    public Set<String> keys() {
        return RUNNING.keySet();
    }

    @Override
    public Vertx vertx(final String name) {
        final RunVertx runVertx = RUNNING.getOr(name);
        return Objects.isNull(runVertx) ? null : runVertx.instance();
    }

    @Override
    public Vertx vertx() {
        if (1 != RUNNING.values().size()) {
            throw Ut.Bnd.failWeb(_501NotSupportException.class, this.getClass());
        }
        final RunVertx runVertx = RUNNING.values().iterator().next();
        return Objects.isNull(runVertx) ? null : runVertx.instance();
    }

    @Override
    public RunVertx valueGet(final int hashCode) {
        final String key = String.valueOf(hashCode);
        final RunVertx runVertx = RUNNING.getOr(key);
        if (runVertx.isOk() && runVertx.isOk(hashCode)) {
            return runVertx;
        }
        return null;
    }

    @Override
    public StoreVertx add(final RunVertx runVertx) {
        Objects.requireNonNull(runVertx);
        if (runVertx.isOk()) {
            final Vertx vertxRef = runVertx.instance();
            final String id = String.valueOf(vertxRef.hashCode());
            RUNNING.put(id, runVertx, runVertx.name());
        }
        return this;
    }

    @Override
    public RunVertx valueGet(final String hashCode) {
        final RunVertx runVertx = RUNNING.getOr(hashCode);
        if (runVertx.isOk()) {
            return runVertx;
        }
        return null;
    }

    @Override
    public StoreVertx remove(final String name) {
        if (Ut.isNotNil(name)) {
            RUNNING.remove(name);
        }
        return this;
    }
}
