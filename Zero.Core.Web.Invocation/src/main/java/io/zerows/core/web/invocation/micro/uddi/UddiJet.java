package io.zerows.core.web.invocation.micro.uddi;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.RoutingContext;

public interface UddiJet {

    UddiJet bind(HttpServerOptions options);

    UddiJet bind(Vertx vertx);

    Handler<RoutingContext> handler();
}
