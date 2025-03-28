package io.zerows.core.configuration.zdk;

import io.horizon.eon.VString;
import io.horizon.eon.em.web.ServerType;
import io.vertx.core.http.HttpServerOptions;

/**
 * @author lang : 2024-04-20
 */
public abstract class AbstractOptionBridge<T> implements OptionOfServer<T> {

    protected HttpServerOptions serverBridge;
    protected T serverOptions;
    protected String serverName;

    protected AbstractOptionBridge(final String serverName) {
        this.serverName = serverName;
    }

    // Overwrite
    @Override
    public OptionOfServer<T> serverBridge(final HttpServerOptions options) {
        this.serverBridge = options;
        return this;
    }

    // Overwrite
    @Override
    public String name() {
        return this.serverName;
    }

    // Overwrite
    @Override
    public String key() {
        final String key = this.serverBridge.getHost() + VString.COLON + this.serverBridge.getPort();
        final String type = this.type().key();
        return type + VString.SLASH + key;
    }

    // Overwrite
    @Override
    public HttpServerOptions serverBridge() {
        return this.serverBridge;
    }

    @Override
    public T options() {
        return this.serverOptions;
    }

    @Override
    public OptionOfServer<T> options(final T option) {
        this.serverOptions = option;
        return this;
    }

    @Override
    public abstract ServerType type();
}
