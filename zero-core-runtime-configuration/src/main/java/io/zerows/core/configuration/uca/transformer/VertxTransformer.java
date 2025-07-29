package io.zerows.core.configuration.uca.transformer;

import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.fn.Fn;
import io.zerows.core.configuration.zdk.Transformer;

import java.util.Objects;

public class VertxTransformer implements Transformer<VertxOptions> {

    public static VertxOptions nativeOption() {
        final VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(3000_000_000_000L);
        options.setMaxWorkerExecuteTime(3000_000_000_000L);
        options.setBlockedThreadCheckInterval(10000);
        options.setPreferNativeTransport(true);
        return options;
    }

    @Override
    public VertxOptions transform(final JsonObject input) {
        final JsonObject config = input.getJsonObject(YmlCore.vertx.OPTIONS, null);
        final VertxOptions options = Fn.runOr(null == config, this.logger(),
            VertxOptions::new,
            () -> {
                assert Objects.nonNull(config) : "`config` should not be null";
                return new VertxOptions(config);
            });
        assert options != null;
        options.setPreferNativeTransport(true);
        return options;
    }
}