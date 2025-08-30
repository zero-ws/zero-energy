package io.zerows.core.web.io.uca.response.hooker;

import io.zerows.core.uca.cache.Cc;
import io.zerows.core.uca.log.Annal;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;

/**
 * @author lang : 2024-04-04
 */
public abstract class AbstractLater<T> implements Later<T> {

    @SuppressWarnings("all")
    static Cc<String, Later> CCT_LATER = Cc.openThread();
    protected RoutingContext context;

    protected AbstractLater(final RoutingContext context) {
        this.context = context;
    }

    protected EventBus eventbus() {
        final Vertx vertx = this.context.vertx();
        return vertx.eventBus();
    }

    protected Session session() {
        return this.context.session();
    }

    protected Annal logger() {
        return Annal.get(this.getClass());
    }
}
