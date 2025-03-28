package io.zerows.core.web.container.uca.mode;

import io.horizon.eon.VValue;
import io.horizon.exception.WebException;
import io.horizon.fn.Actuator;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.Cookie;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.up.annotations.Address;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;
import io.zerows.core.web.container.exception._500DeliveryErrorException;
import io.zerows.core.web.container.exception._500EntityCastException;
import io.zerows.core.web.invocation.uca.runner.InvokerUtil;
import io.zerows.core.web.io.atom.WrapRequest;
import io.zerows.core.web.io.uca.request.mime.Analyzer;
import io.zerows.core.web.io.uca.request.mime.MediaAnalyzer;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.atom.Rule;
import io.zerows.core.web.model.commune.Envelop;
import io.zerows.core.web.validation.ValidatorEntry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Base class to provide template method
 */
public abstract class AbstractAim {
    private static final Cc<String, Analyzer> CC_ANALYZER = Cc.openThread();
    private static final Cc<String, ValidatorEntry> CC_VALIDATOR = Cc.openThread();

    private transient final Analyzer analyzer = CC_ANALYZER.pick(MediaAnalyzer::new, MediaAnalyzer.class.getName());
    // Fn.po?lThread(POOL_ANALYZER, MediaAnalyzer::new, MediaAnalyzer.class.getName());
    private transient final ValidatorEntry verifier = CC_VALIDATOR.pick(ValidatorEntry::new);
    // Fn.po?lThread(POOL_VALIDATOR, Validator::new);

    /**
     * Template method
     *
     * @param context RoutingContext reference
     * @param event   Event object of definition
     *
     * @return TypedArgument ( Object[] )
     */
    protected Object[] buildArgs(final RoutingContext context,
                                 final Event event) {
        Object[] cached = context.get(KWeb.ARGS.REQUEST_CACHED);
        if (null == cached) {
            cached = this.analyzer.in(context, event);
            context.put(KWeb.ARGS.REQUEST_CACHED, cached);
        }
        // Validation handler has been getNull the parameters.
        return cached;
    }


    /**
     * Get event bus address.
     *
     * @param event Event object of definition
     *
     * @return Get event bus address
     */
    protected String address(final Event event) {
        final Method method = event.getAction();
        final Annotation annotation = method.getDeclaredAnnotation(Address.class);
        return Ut.invoke(annotation, "value");
    }

    /**
     * @param event Event object of definition
     * @param args  TypedArgument ( Object[] )
     *
     * @return Return invoked result
     */
    protected Object invoke(final Event event, final Object[] args) {
        final Method method = event.getAction();
        this.getLogger().info("Class = {2}, Method = {0}, Args = {1}",
            method.getName(), Ut.fromJoin(args), method.getDeclaringClass().getName());
        return InvokerUtil.invoke(event.getProxy(), method, args);
    }

    protected Envelop failure(final String address,
                              final AsyncResult<Message<Envelop>> handler) {
        final Throwable cause = handler.cause();
        final WebException error;
        if (Objects.isNull(cause)) {
            error = new _500DeliveryErrorException(this.getClass(),
                address, "Unknown");
        } else {
            if (cause instanceof WebException) {
                error = (WebException) cause;
            } else {
                error = new _500DeliveryErrorException(this.getClass(),
                    address, "Jvm: " + cause.getMessage());
            }
        }
        return Envelop.failure(error);
    }

    protected Envelop success(final String address,
                              final AsyncResult<Message<Envelop>> handler
    ) {
        Envelop envelop;
        try {
            final Message<Envelop> message = handler.result();
            envelop = message.body();
        } catch (final Throwable ex) {
            final WebException error
                = new _500EntityCastException(this.getClass(),
                address, ex.getMessage());
            envelop = Envelop.failure(error);
        }
        return envelop;
    }

    protected ValidatorEntry verifier() {
        return this.verifier;
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    protected void executeRequest(final RoutingContext context,
                                  final Map<String, List<Rule>> rulers,
                                  final WrapRequest wrapRequest) {
        try {
            final Object[] args = this.buildArgs(context, wrapRequest.getEvent());
            // Execute web flow and uniform call.
            Flower.executeRequest(context, rulers, wrapRequest, args, this.verifier());
        } catch (final WebException error) {
            // Bad request of 400 for parameter processing
            Flower.replyError(context, error, wrapRequest.getEvent());
        } catch (final Exception ex) {
            // DEBUG:
            ex.printStackTrace();
        }
    }

    protected void exec(final Actuator consumer,
                        final RoutingContext context,
                        final Event event) {
        try {
            // Monitor
            this.getLogger().debug("Web flow started: {0}", event.getAction());
            {
                final Session session = context.session();
                if (Objects.nonNull(session)) {
                    // Fix: 3.9.1 cookie error of null dot
                    final Cookie cookie = context.request().getCookie(VValue.DEFAULT_SESSION);
                    this.getLogger().debug(INFO.SESSION_ID, context.request().path(),
                        session.id(), Objects.isNull(cookie) ? null : cookie.getValue());
                }
            }
            consumer.execute();
        } catch (final WebException ex) {
            Flower.replyError(context, ex, event);
        }
    }
}
