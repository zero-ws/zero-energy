package io.zerows.core.feature.web.cache;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.feature.web.cache.shared.UxPool;
import io.zerows.core.metadata.uca.logging.OLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AbstractRapid<K, T> implements Rapid<K, T> {
    protected final transient UxPool pool;
    protected final transient int expired;

    protected AbstractRapid(final String poolName, final int expired) {
        this.pool = UxPool.of(poolName);
        this.expired = expired;
    }

    protected AbstractRapid(final User user) {
        Objects.requireNonNull(user);
        this.expired = -1;
        final JsonObject credit = Ut.valueJObject(user.principal());
        final String poolName = credit.getString(KName.HABITUS);
        this.pool = UxPool.of(poolName);
    }

    protected OLog logger() {
        return Ut.Log.cache(this.getClass());
    }

    @Override
    public Future<T> write(final K key, final T value) {
        if (0 < this.expired) {
            return this.pool.put(key, value, this.expired)
                .compose(kv -> Ut.future(kv.value()));
        } else {
            return this.pool.put(key, value)
                .compose(kv -> Ut.future(kv.value()));
        }
    }

    @Override
    public Future<T> writeMulti(final Set<K> keySet, final T value) {
        final List<Future<T>> futures = new ArrayList<>();
        keySet.forEach(key -> futures.add(this.write(key, value)));
        return Fn.combineT(futures).compose(nil -> Ut.future(value));
    }

    @Override
    public Future<Boolean> writeMulti(final Set<K> keySet) {
        final List<Future<T>> futures = new ArrayList<>();
        keySet.forEach(key -> futures.add(this.clear(key)));
        return Fn.combineT(futures).compose(nil -> Ut.futureT());
    }

    @Override
    public Future<T> clear(final K key) {
        return this.pool.<K, T>remove(key)
            .compose(kv -> Ut.future(kv.value()));
    }

    @Override
    public Future<T> read(final K key) {
        return this.pool.get(key);
    }
}
