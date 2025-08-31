package io.zerows.core.database.cache.hit;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.zerows.ams.constant.em.typed.ChangeFlag;
import io.zerows.core.database.cache.l1.L1Cache;
import io.zerows.core.database.cache.l1.L1Config;
import io.zerows.core.util.Ut;
import io.zerows.module.metadata.uca.logging.OLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Abstract class for uniform processing for L1 cache
 */
@SuppressWarnings("all")
public abstract class AbstractL1 implements L1Cache {
    protected transient Vertx vertx;
    protected transient L1Config config;

    /*
     * vertxRef reference for
     */
    @Override
    public L1Cache bind(final L1Config config) {
        this.config = config;
        return this;
    }

    @Override
    public L1Cache bind(final Vertx vertx) {
        this.vertx = vertx;
        return this;
    }

    /*
     * Refresh will send data to event bus
     * Publish / Consume mode for cache data processing
     * Actual the cache will be deleted
     */
    @Override
    public void write(final CMessage... messages) {
        this.publish(ChangeFlag.UPDATE, messages);
    }

    @Override
    public void delete(final CMessage... messages) {
        this.publish(ChangeFlag.DELETE, messages);
    }

    @Override
    public <T> Future<T> readAsync(final CMessage message) {
        // Get key
        final String uk = message.dataKey();
        return this.readCacheAsync(uk).compose(response ->
            /*
             * Future<Tool> returned
             */
            Future.succeededFuture(this.deserialize(response, message.dataType())));
    }

    @Override
    public <T> T read(final CMessage message) {
        // Get key
        final String uk = message.dataKey();
        final Object response = this.readCache(uk);
        return this.deserialize(response, message.dataType());
    }

    @Override
    public Boolean exist(final CMessage message) {
        return Objects.nonNull(this.read(message));
    }

    @Override
    public Future<Boolean> existAsync(final CMessage message) {
        return this.readAsync(message).compose(item -> Future.succeededFuture(Objects.nonNull(item)));
    }

    // ------------------ Private Method -------------------------
    private void publish(final ChangeFlag flag, final CMessage... messages) {
        final String address = this.config.getAddress();
        if (Ut.isNotNil(address)) {
            final EventBus eventBus = this.vertx.eventBus();
            /*
             * Delivery Message extraction
             */
            Arrays.asList(messages)
                /*
                 * Publish message to event bus
                 */
                .forEach(message -> eventBus.publish(address, message.dataDelivery(flag)));
        }
    }

    private <T> T deserialize(final Object response, final Class<?> dataType) {
        if (Objects.isNull(response)) {
            return null;
        } else {
            final T ret;
            if (response instanceof JsonObject) {
                ret = (T) Ut.deserialize((JsonObject) response, dataType);
            } else if (response instanceof JsonArray) {
                /*
                 * Combine single and multi
                 */
                final List resultList = new ArrayList();
                Ut.itJArray((JsonArray) response).forEach(json -> resultList.add(Ut.deserialize(json, dataType)));
                ret = (T) resultList;
            } else {
                ret = null;
            }
            return ret;
        }
    }

    protected OLog logger() {
        return Ut.Log.cache(this.getClass());
    }

    // ------------------ Abstract Method -------------------------

    public abstract <T> Future<T> readCacheAsync(String key);

    public abstract <T> T readCache(String key);
}
