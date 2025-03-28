package io.zerows.core.web.invocation.uca.runner;

import io.horizon.eon.VValue;
import io.modello.eon.em.EmValue;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.Message;
import io.vertx.up.annotations.Me;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.web.invocation.micro.uddi.Uddi;
import io.zerows.core.web.invocation.micro.uddi.UddiClient;
import io.zerows.core.web.model.commune.Envelop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Function;

/**
 * Uniform call TunnelClient to remove duplicated codes
 * Refactor invokder to support Dynamic Invoke
 */
@SuppressWarnings("all")
public abstract class AbstractInvoker implements Invoker {

    protected OLog logger() {
        return Ut.Log.uca(this.getClass());
    }

    /**
     * Future method(JsonObject)
     * Future method(JsonArray)
     */
    protected Future invokeJson(
        final Object proxy,
        final Method method,
        final Envelop envelop) {
        // Preparing Method
        invokePre(method, envelop);
        final Object reference = envelop.data();
        final Class<?> argType = method.getParameterTypes()[VValue.IDX];
        final Object arguments = Ut.deserialize(Ut.toString(reference), argType);
        return InvokerUtil.invoke(proxy, method, arguments);
    }

    private void invokePre(final Method method, final Envelop envelop) {
        if (method.isAnnotationPresent(Me.class)) {
            final Annotation annotation = method.getDeclaredAnnotation(Me.class);
            final EmValue.Bool active = Ut.invoke(annotation, "active");
            final boolean app = Ut.invoke(annotation, "app");
            envelop.onMe(active, app);
        }
    }

    protected <I> Envelop invokeWrap(final I input) {
        if (input instanceof Envelop) {
            // Return Envelop directly
            return (Envelop) input;
        } else {
            // Return Envelop building
            return Envelop.success(input);
        }
    }

    /**
     * R method(Tool..)
     */
    protected Object invokeInternal(
        final Object proxy,
        final Method method,
        final Envelop envelop
    ) {
        // Preparing Method
        invokePre(method, envelop);
        // Return value here.
        return InvokerUtil.invokeCall(proxy, method, envelop);
    }

    protected <T> Handler<AsyncResult<T>> invokeHandler(final Message<Envelop> message) {
        return handler -> {
            if (handler.succeeded()) {
                message.reply(Envelop.moveOn(handler.result()));
            } else {
                // Readible codec for configured information, error flow needed.
                if (null != handler.cause()) {
                    handler.cause().printStackTrace();
                }
                message.reply(Envelop.failure(Ut.failWeb(getClass(), handler.cause())));
            }
        };
    }

    /**
     *
     */
    protected <I> Function<I, Future<Envelop>> nextEnvelop(
        final Vertx vertx,
        final Method method) {
        return item -> this.nextEnvelop(vertx, method, item);
    }

    protected <T> Future<Envelop> nextEnvelop(
        final Vertx vertx,
        final Method method,
        final T result
    ) {
        final UddiClient client = Uddi.client(getClass());
        return client.bind(vertx).bind(method).connect(Envelop.moveOn(result));
    }
}
