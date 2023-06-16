package io.vertx.up.boot.anima;

import io.horizon.eon.em.web.ServerType;
import io.macrocosm.specification.config.HConfig;

import java.util.concurrent.ConcurrentMap;

/**
 * Start Up condition for different bottle deployment.
 */
public interface Factor {
    ConcurrentMap<ServerType, Class<?>> endpoint(HConfig config);
}
