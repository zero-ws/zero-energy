package io.zerows.core.configuration.uca.transformer;

import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.configuration.atom.option.SockOptions;
import io.zerows.core.configuration.zdk.Transformer;

/**
 * @author lang : 2024-04-20
 */
public class SockTransformer implements Transformer<SockOptions> {
    @Override
    public SockOptions transform(final JsonObject config) {
        return Fn.runOr(null == config, this.logger(), SockOptions::new, () -> {
            /*
             * websocket:       ( SockOptions )
             * config:          ( HttpServerOptions )
             */
            final JsonObject websockJ = Ut.valueJObject(config, KName.WEB_SOCKET);
            final SockOptions options = Ut.deserialize(websockJ, SockOptions.class);

            /*
             * Bind the HttpServerOptions to SockOptions for future usage
             * 此处结合 WebSocket 的配置结构执行转换，生成对应的 HttpServerOptions
             *
             */
            final JsonObject optionJ = Ut.valueJObject(config, KName.CONFIG);
            final HttpServerOptions serverOptions = new HttpServerOptions(optionJ);

            options.options(serverOptions);
            return options;
        });
    }
}
