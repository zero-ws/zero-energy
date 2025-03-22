package io.zerows.core.metadata.zdk.plugins;

import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.exception.BootKeyMissingException;
import io.zerows.core.metadata.store.OZeroStore;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.metadata.uca.stable.Ruler;

import java.util.function.Function;

public interface Infix {
    static <R> R init(final String key,
                      final Function<JsonObject, R> executor,
                      final Class<?> clazz) {
        final OLog logger = Ut.Log.plugin(clazz);
        Fn.outBoot(!OZeroStore.is(key), logger, BootKeyMissingException.class,
            clazz, key);
        final JsonObject options = OZeroStore.option(key);
        Fn.outBug(() -> Ruler.verify(key, options), logger);
        return executor.apply(options);
    }

    <T> T get();
}
