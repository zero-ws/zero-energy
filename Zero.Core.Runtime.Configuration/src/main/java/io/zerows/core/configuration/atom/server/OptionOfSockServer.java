package io.zerows.core.configuration.atom.server;

import io.horizon.eon.em.web.ServerType;
import io.zerows.core.configuration.atom.option.SockOptions;
import io.zerows.core.configuration.zdk.AbstractOptionBridge;
import io.zerows.core.configuration.zdk.OptionOfServer;

/**
 * @author lang : 2024-04-20
 */
class OptionOfSockServer extends AbstractOptionBridge<SockOptions> {

    private OptionOfSockServer(final String name) {
        super(name);
    }

    static OptionOfServer<SockOptions> of(final String name, final SockOptions options) {
        return new OptionOfSockServer(name).options(options);
    }

    @Override
    public ServerType type() {
        return ServerType.SOCK;
    }
}
