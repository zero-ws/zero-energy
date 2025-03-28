package io.zerows.core.web.model.uca.extract;

import io.horizon.uca.log.Annal;
import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.http.HttpMethod;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.exception.BootCodexMoreException;
import io.zerows.core.web.model.exception.BootEventSourceException;
import jakarta.ws.rs.Path;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Scanned @EndPoint clazz to build Event metadata
 */
public class ExtractorEvent implements Extractor<Set<Event>> {

    private static final Annal LOGGER = Annal.get(ExtractorEvent.class);

    @Override
    public Set<Event> extract(final Class<?> clazz) {
        return Fn.runOr(new HashSet<>(), () -> {
            // 1. Class verify
            this.verify(clazz);
            // 2. Check whether clazz annotated with @PATH
            final Set<Event> result = new HashSet<>();
            Fn.runAt(clazz.isAnnotationPresent(Path.class), LOGGER,
                () -> {
                    // 3.1. Append Root Path
                    final Path path = this.path(clazz);
                    assert null != path : "Path should not be null.";
                    result.addAll(this.extract(clazz, ToolPath.resolve(path)));
                },
                () -> {
                    // 3.2. Use method Path directly
                    result.addAll(this.extract(clazz, null));
                });
            return result;
        }, clazz);
    }

    private void verify(final Class<?> clazz) {
        // Check basic specification: No Arg Constructor
        if (!clazz.isInterface()) {
            // Class direct.
            ToolVerifier.noArg(clazz, this.getClass());
        }
        ToolVerifier.modifier(clazz, this.getClass());
        // Event Source Checking
        Fn.outBoot(!clazz.isAnnotationPresent(EndPoint.class),
            LOGGER, BootEventSourceException.class,
            this.getClass(), clazz.getName());
    }

    @SuppressWarnings("all")
    private Set<Event> extract(final Class<?> clazz, final String root) {
        final Set<Event> events = new HashSet<>();
        // 0.Preparing
        final Method[] methods = clazz.getDeclaredMethods();
        // 1.Validate Codex annotation appears
        final Long counter = Observable.fromArray(methods)
            .map(Method::getParameterAnnotations)
            .flatMap(Observable::fromArray)
            .map(Arrays::asList)
            .map(item -> item.stream().map(Annotation::annotationType).collect(Collectors.toList()))
            .filter(item -> item.contains(Codex.class))
            .count().blockingGet();
        Fn.outBoot(methods.length < counter, LOGGER,
            BootCodexMoreException.class,
            this.getClass(), clazz);
        // 2.Build Set
        events.addAll(Arrays.stream(methods).filter(ToolMethod::isValid)
            .map(item -> this.extract(item, root))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet()));
        // 3.Break the Event `priority` draw down.
        return events;
    }

    /**
     * Scan for single
     *
     * @param method single method that will be scanned.
     * @param root   root path calculation
     *
     * @return Standard Event object
     */
    private Event extract(final Method method, final String root) {
        // 1.Method path
        final Event event = new Event();
        // 2.Method resolve
        final HttpMethod httpMethod = ToolMethod.resolve(method);
        if (null == httpMethod) {
            // Ignored the method could not be annotated.
            LOGGER.warn("\u001b[0;31m!!!!!, Missed HttpMethod annotation for method\u001b[m ? (GET,POST,PUT,...). method = \u001b[0;31m{0}\u001b[m", method);
            return null;
        } else {
            event.setMethod(httpMethod);
        }
        {
            // 3.1. Get path from method
            final Path path = this.path(method);
            if (null == path) {
                // 3.2. Check root double check
                if (!Ut.isNil(root)) {
                    // Use root directly.
                    event.setPath(root);
                }
            } else {
                final String result = ToolPath.resolve(
                    path, root);
                event.setPath(result);
            }
        }
        // 4.Action
        event.setAction(method);
        // 6.Mime resolve
        event.setConsumes(ToolMedia.consumes(method));
        event.setProduces(ToolMedia.produces(method));
        // 7. Instance clazz for proxy
        final Class<?> clazz = method.getDeclaringClass();
        event.setProxy(clazz);
        // 8. Order
        if (method.isAnnotationPresent(Adjust.class)) {
            final Annotation adjust = method.getDeclaredAnnotation(Adjust.class);
            final Integer order = Ut.invoke(adjust, KName.VALUE);
            if (Objects.nonNull(order)) {
                /*
                 * Routing order modification.
                 */
                event.setOrder(order);
            }
        }
        return event;
    }


    private Path path(final Class<?> clazz) {
        return this.path(clazz.getDeclaredAnnotation(Path.class));
    }

    private Path path(final Method method) {
        return this.path(method.getDeclaredAnnotation(Path.class));
    }

    private Path path(final Annotation anno) {
        return (anno instanceof Path) ? (Path) anno : null;
    }
}
