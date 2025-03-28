package io.zerows.core.feature.web.session;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.annotations.Infusion;
import io.vertx.up.eon.configure.YmlCore;
import io.zerows.core.metadata.zdk.plugins.Infix;

import java.util.Objects;

@Infusion
@SuppressWarnings("unchecked")
public class SessionInfix implements Infix {

    private static final String NAME = "ZERO_SESSION_POOL";
    private static final Cc<String, SessionClient> CC_CLIENTS = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        getOrCreate(vertx, name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static SessionClient getClient() {
        return CC_CLIENTS.store(NAME);
    }

    public static SessionClient getOrCreate(final Vertx vertx) {
        return getOrCreate(vertx, NAME);
    }

    public static SessionClient getOrCreate(final Vertx vertx, final JsonObject inputConfig) {
        return getOrCreate(vertx, NAME, inputConfig);
    }

    public static SessionClient getOrCreate(final Vertx vertx, final String name) {
        return getOrCreate(vertx, name, null);
    }

    private static SessionClient getOrCreate(final Vertx vertx, final String name, final JsonObject inputConfig) {
        final SessionClient client = CC_CLIENTS.store(name);
        if (Objects.isNull(client)) {
            /* Null will create new */
            return CC_CLIENTS.pick(() -> {
                if (Objects.isNull(inputConfig)) {
                    // Infix 架构处理（不适合OSGI）
                    return Infix.init(YmlCore.inject.SESSION,
                        (config) -> SessionClient.createShared(vertx, config),
                        SessionInfix.class
                    );
                } else {
                    // 外层传入（OSGI环境专用）
                    return SessionClient.createShared(vertx, inputConfig);
                }
            }, name);
        } else {
            /*
             * Not null, it will get previous reference
             */
            return client;
        }
    }

    @Override
    public SessionClient get() {
        return getClient();
    }
}
