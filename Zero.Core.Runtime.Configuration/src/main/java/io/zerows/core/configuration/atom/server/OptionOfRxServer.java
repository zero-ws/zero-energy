package io.zerows.core.configuration.atom.server;

import io.horizon.eon.em.web.ServerType;
import io.vertx.core.http.HttpServerOptions;
import io.zerows.core.configuration.zdk.AbstractOptionBridge;
import io.zerows.core.configuration.zdk.OptionOfServer;

/**
 * @author lang : 2024-04-20
 */
class OptionOfRxServer extends AbstractOptionBridge<HttpServerOptions> {

    private OptionOfRxServer(final String name) {
        super(name);
    }

    static OptionOfServer<HttpServerOptions> of(final String name, final HttpServerOptions options) {
        return new OptionOfRxServer(name).options(options);
    }

    @Override
    public ServerType type() {
        return ServerType.API;
    }
}
