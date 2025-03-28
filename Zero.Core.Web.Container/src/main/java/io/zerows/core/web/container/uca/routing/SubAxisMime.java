package io.zerows.core.web.container.uca.routing;

import io.horizon.eon.VString;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.ext.web.Route;
import io.zerows.core.web.io.uca.routing.OAxisSub;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.atom.running.RunRoute;
import jakarta.ws.rs.core.MediaType;
import org.osgi.framework.Bundle;

import java.util.Set;

/**
 * @author lang : 2024-05-04
 */
public class SubAxisMime implements OAxisSub {
    @Override
    public void mount(final RunRoute runRoute, final Bundle bundle) {
        final Event event = runRoute.refEvent();
        final Route route = runRoute.instance();
        // produces
        final Set<MediaType> produces = event.getProduces();
        Observable.fromIterable(produces)
            .map(type -> type.getType() + VString.SLASH + type.getSubtype())
            .subscribe(route::produces).dispose();
        // consumes
        final Set<MediaType> consumes = event.getConsumes();
        Observable.fromIterable(consumes)
            .map(type -> type.getType() + VString.SLASH + type.getSubtype())
            .subscribe(route::consumes).dispose();
    }
}
