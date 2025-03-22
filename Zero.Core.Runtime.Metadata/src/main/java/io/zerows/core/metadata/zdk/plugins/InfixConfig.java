package io.zerows.core.metadata.zdk.plugins;

import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.exception.BootDynamicKeyMissingException;
import io.zerows.core.metadata.store.OZeroStore;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.metadata.uca.stable.Ruler;

import java.io.Serializable;

/**
 * Third part configuration data.
 * endpoint: Major endpoint
 * config: Configuration Data of third part.
 */
public class InfixConfig implements Serializable {

    private static final OLog LOGGER = Ut.Log.configure(InfixConfig.class);

    private static final Cc<String, InfixConfig> CC_CACHE = Cc.open();

    private static final String KEY_ENDPOINT = "endpoint";
    private static final String KEY_CONFIG = "config";

    private final transient JsonObject config;
    private final transient String endpoint;

    public InfixConfig(final String key, final String rule) {
        final JsonObject raw = OZeroStore.option(key);
        // Check up exception for key
        Fn.outBoot(!OZeroStore.is(key),
            LOGGER, BootDynamicKeyMissingException.class,
            this.getClass(), key, raw);

        // Check up exception for JsonObject
        this.endpoint = Fn.runOr(null, () -> raw.getString(KEY_ENDPOINT), raw.getValue(KEY_ENDPOINT));
        this.config = Fn.runOr(new JsonObject(), () -> raw.getJsonObject(KEY_CONFIG), raw.getValue(KEY_CONFIG));
        // Verify the config data.
        if (null != rule) {
            Fn.outBug(() -> Fn.bugAt(() -> Ruler.verify(rule, this.config), this.config), LOGGER);
        }
    }

    public static InfixConfig create(final String key) {
        return CC_CACHE.pick(() -> new InfixConfig(key, null), key);
        // return Fn.po?l(CACHE, key, () -> new InfixConfig(key, null));
    }

    public static InfixConfig create(final String key, final String rule) {
        return CC_CACHE.pick(() -> new InfixConfig(key, rule), key);
        // return Fn.po?l(CACHE, key, () -> new InfixConfig(key, rule));
    }

    public JsonObject getConfig() {
        return this.config;
    }

    public String getEndPoint() {
        return this.endpoint;
    }
}
