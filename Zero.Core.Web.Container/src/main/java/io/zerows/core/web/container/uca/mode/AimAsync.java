package io.zerows.core.web.container.uca.mode;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.zerows.core.assembly.atom.OProxyInstance;
import io.zerows.core.configuration.atom.NodeNetwork;
import io.zerows.core.configuration.store.OCacheNode;
import io.zerows.core.web.io.zdk.Aim;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.commune.Envelop;

import java.util.Objects;

public class AimAsync extends AbstractAim implements Aim<RoutingContext> {

    @Override
    public Handler<RoutingContext> attack(final Event event) {
        return Fn.runOr(() -> (context) -> this.exec(() -> {
            /*
             * Build future ( data handler )
             */
            final Future<Envelop> future = this.invoke(context, event);
            /*
             * Event bus building / get from vertx instance.
             */
            final Vertx vertx = context.vertx();
            final EventBus bus = vertx.eventBus();
            final String address = this.address(event);

            /*
             * New method instead of old
             * -- request(address, Tool, handler)
             */
            future.onComplete(dataRes -> {
                /*
                 * To avoid null dot result when the handler triggered result here
                 * SUCCESS
                 */
                if (dataRes.succeeded()) {
                    /*
                     * Before send, captured the previous, only this kind of situation need
                     * Do `combine` request here.
                     * Because:
                     *
                     * - In OneWay, the client do not focus on response data.
                     * - In Ping, the client also get `true/false` only
                     * - In Sync, not need to pass Envelop on event bus
                     */
                    final Envelop request = dataRes.result();

                    final NodeNetwork network = OCacheNode.of().network();
                    final DeliveryOptions deliveryOptions = network.get().optionDelivery();
                    bus.<Envelop>request(address, request, deliveryOptions, handler -> {
                        final Envelop response;
                        if (handler.succeeded()) {
                            // Request - Response message
                            response = this.success(address, handler);
                        } else {
                            response = this.failure(address, handler);
                        }
                        // Request -> Response
                        response.from(request);
                        Answer.reply(context, response, event);
                    });
                } else {
                    if (Objects.nonNull(dataRes.cause())) {
                        dataRes.cause().printStackTrace();
                    }
                    /*
                     * Error Replying
                     */
                    Answer.reply(context, Envelop.failure(dataRes.cause()));
                }
            });
        }, context, event), event);
    }

    private Future<Envelop> invoke(final RoutingContext context,
                                   final Event event) {
        final Object proxy = event.getProxy();
        /*
         * Method arguments building here.
         */
        final Object[] arguments = this.buildArgs(context, event);
        /*
         * Whether it's interface mode or agent mode
         */
        final Future<Envelop> invoked;
        if (proxy instanceof OProxyInstance) {
            final JsonObject message = new JsonObject();
            for (int idx = 0; idx < arguments.length; idx++) {
                message.put(String.valueOf(idx), arguments[idx]);
            }
            /*
             * Interface mode
             */
            invoked = Flower.next(context, message);
        } else {
            /*
             * Agent mode
             */
            final Object returnValue = this.invoke(event, arguments);
            invoked = Flower.next(context, returnValue);
        }

        return invoked.compose(response -> {
            /*
             * The next method of compose for future building assist data such as
             * Headers,
             * User,
             * Session
             * Context
             * It's critical for Envelop object when communication
             */
            response.bind(context);
            return Ux.future(response);
        });
    }
}
