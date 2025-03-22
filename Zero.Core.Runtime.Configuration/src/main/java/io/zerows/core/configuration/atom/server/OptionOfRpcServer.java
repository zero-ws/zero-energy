package io.zerows.core.configuration.atom.server;

import io.horizon.eon.em.web.ServerType;
import io.zerows.core.configuration.atom.option.RpcOptions;
import io.zerows.core.configuration.zdk.AbstractOptionBridge;
import io.zerows.core.configuration.zdk.OptionOfServer;

/**
 * @author lang : 2024-04-20
 */
class OptionOfRpcServer extends AbstractOptionBridge<RpcOptions> {

    private OptionOfRpcServer(final String name) {
        super(name);
    }

    static OptionOfServer<RpcOptions> of(final String name, final RpcOptions options) {
        return new OptionOfRpcServer(name).options(options);
    }

    @Override
    public ServerType type() {
        return ServerType.IPC;
    }
}
