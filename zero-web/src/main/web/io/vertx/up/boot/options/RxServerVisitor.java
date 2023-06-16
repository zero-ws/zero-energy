package io.vertx.up.boot.options;

import io.horizon.eon.em.web.ServerType;

public class RxServerVisitor extends HttpServerVisitor {

    @Override
    public ServerType serverType() {
        return ServerType.RX;
    }
}
