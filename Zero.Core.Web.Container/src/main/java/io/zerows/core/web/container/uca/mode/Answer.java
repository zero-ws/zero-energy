package io.zerows.core.web.container.uca.mode;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.web._500InternalServerException;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.eon.KWeb;
import io.zerows.core.web.container.uca.reply.ActionReply;
import io.zerows.core.web.container.uca.reply.OAmbit;
import io.zerows.core.web.io.uca.response.hooker.Later;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.commune.Envelop;
import jakarta.ws.rs.core.MediaType;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Response process to normalize the response request.
 * 1. Media definition
 * 2. Operation based on event, envelop, context
 */
public final class Answer {

    public static Envelop previous(final RoutingContext context) {
        Envelop envelop = context.get(KWeb.ARGS.REQUEST_BODY);
        if (Objects.isNull(envelop)) {
            envelop = Envelop.failure(new _500InternalServerException(Answer.class, "Previous Error of " + KWeb.ARGS.REQUEST_BODY));
        }
        return envelop;
    }

    public static void next(final RoutingContext context, final Envelop envelop) {
        if (envelop.valid()) {
            /*
             * Next step here
             */
            context.put(KWeb.ARGS.REQUEST_BODY, envelop);
            context.next();
        } else {
            reply(context, envelop);
        }
    }

    public static void normalize(final RoutingContext context, final Envelop envelop) {
        if (envelop.valid()) {
            /*
             * Updated here
             */
            envelop.bind(context);
            context.put(KWeb.ARGS.REQUEST_BODY, envelop);
            context.next();
        } else {
            reply(context, envelop);
        }
    }

    public static void reply(final RoutingContext context, final Envelop envelop) {
        reply(context, envelop, new HashSet<>());
    }

    public static void reply(final RoutingContext context, final Envelop envelop, final Supplier<Set<MediaType>> supplier) {
        Set<MediaType> produces = Objects.isNull(supplier) ? new HashSet<>() : supplier.get();
        if (Objects.isNull(produces)) {
            produces = new HashSet<>();
        }
        reply(context, envelop, produces);
    }

    public static void reply(final RoutingContext context, final Envelop envelop, final Event event) {
        Set<MediaType> produces;
        if (Objects.isNull(event)) {
            produces = new HashSet<>();
        } else {
            produces = event.getProduces();
            if (Objects.isNull(produces)) {
                produces = new HashSet<>();
            }
        }
        reply(context, envelop, produces, Objects.isNull(event) ? null : event.getAction());
    }

    private static void reply(final RoutingContext context, final Envelop envelop, final Set<MediaType> mediaTypes) {
        reply(context, envelop, mediaTypes, null);
    }

    private static void reply(final RoutingContext context, final Envelop envelop,
                              final Set<MediaType> mediaTypes, final Method sessionAction) {
        final HttpServerResponse response = context.response();
        /*
         * FIX: java.lang.IllegalStateException: Response is closed
         * The response has been sent here
         */
        if (!response.closed()) {
            /*
             * Set http status information on response, all information came from `Envelop`
             * 1) Status Code
             * 2) Status Message
             * Old code
             *
             *
             *
             * Above code will be put into
             * Outcome.out(response, processed, mediaTypes);
             *
             * It's not needed for current position to set or here will cause following bug:
             *   java.lang.IllegalStateException: Response head already sent
             */
            final HttpStatusCode code = envelop.status();
            response.setStatusCode(code.code());
            response.setStatusMessage(code.message());
            /*
             * Bind Data
             */
            envelop.bind(context);
            /*
             * Media processing
             */
            Outcome.media(response, mediaTypes);
            /*
             * Security for response ( Successful Only )
             */
            if (envelop.valid()) {
                Outcome.security(response);
                {
                    /*
                     * SessionData Stored
                     */
                    final Object data = envelop.data();
                    final Later<Object> later = Later.ofSession(context);
                    later.execute(data, sessionAction);
                }
            }
            /*
             * Infusion Extension for response replying here ( Plug-in )
             */
            OAmbit.of(ActionReply.class).then(context, envelop).compose(processed -> {
                /*
                 * Output of current situation,
                 * Here has been replaced by DataRegion.
                 * Fix BUG:
                 * In old workflow, below code is not in compose of `OAmbit`,
                 * The async will impact response data here, it could let response keep the original
                 * and ACL workflow won't be OK for response data serialization.
                 */
                Outcome.out(response, processed, mediaTypes);

                {
                    /*
                     * New Feature to publish data into address of @Off
                     */
                    final Later<Envelop> later = Later.ofNotify(context);
                    later.execute(processed, sessionAction);
                }
                return Future.succeededFuture();
            });
        }
    }
}
