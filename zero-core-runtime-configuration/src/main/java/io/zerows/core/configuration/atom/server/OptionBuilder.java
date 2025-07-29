package io.zerows.core.configuration.atom.server;

import io.horizon.eon.em.web.ServerType;
import io.vertx.core.http.HttpServerOptions;
import io.zerows.core.configuration.atom.option.RpcOptions;
import io.zerows.core.configuration.atom.option.SockOptions;
import io.zerows.core.configuration.zdk.OptionOfServer;

/**
 * @author lang : 2024-04-20
 */
public class OptionBuilder {


    public static OptionOfServer<HttpServerOptions> ofHttp(final String name,
                                                           final ServerType type,
                                                           final HttpServerOptions options) {
        return switch (type) {
            case HTTP -> OptionOfHttpServer.of(name, options);
            case API -> OptionOfGateway.of(name, options);
            case RX -> OptionOfRxServer.of(name, options);
            default -> throw new IllegalArgumentException("Unknown server type: " + type);
        };
    }

    public static OptionOfServer<SockOptions> ofSock(final String name, final SockOptions options) {
        return OptionOfSockServer.of(name, options);
    }

    public static OptionOfServer<RpcOptions> ofRpc(final String name, final RpcOptions options) {
        return OptionOfRpcServer.of(name, options);
    }
}
