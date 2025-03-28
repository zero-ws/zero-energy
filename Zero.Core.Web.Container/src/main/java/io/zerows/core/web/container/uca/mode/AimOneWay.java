package io.zerows.core.web.container.uca.mode;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.fn.Fn;
import io.zerows.core.configuration.atom.NodeNetwork;
import io.zerows.core.configuration.store.OCacheNode;
import io.zerows.core.web.io.zdk.Aim;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.commune.Envelop;

/**
 * OneWayAim: Event Bus: One-Way
 */
public class AimOneWay extends AbstractAim implements Aim<RoutingContext> {

    @Override
    public Handler<RoutingContext> attack(final Event event) {
        return Fn.runOr(() -> (context) -> this.exec(() -> {
            /*
             * Build TypedArgument by java reflection metadata definition here
             */
            final Object[] arguments = this.buildArgs(context, event);

            /*
             * Method callxx
             * Java reflector to call developer's defined method
             */
            final Object returnValue = this.invoke(event, arguments);

            /*
             * Build event bus
             * This aim is async mode, it should enable Event Bus in new version instead of
             * bus.send here.
             */
            final Vertx vertx = context.vertx();
            final EventBus bus = vertx.eventBus();
            final String address = this.address(event);

            /*
             * Call Flower next method to get future
             * This future is async and we must set handler to capture the result of this future
             * here.
             */
            final Future<Envelop> future = Flower.next(context, returnValue);

            /*
             * Event bus send request out instead of other method
             * Please refer following old code to compare.
             */
            future.onComplete(dataRes -> {
                /*
                 * To avoid null dot result when the handler triggered result here
                 * SUCCESS
                 */
                if (dataRes.succeeded()) {
                    final NodeNetwork network = OCacheNode.of().network();
                    final DeliveryOptions deliveryOptions = network.get().optionDelivery();
                    bus.<Envelop>request(address, dataRes.result(), deliveryOptions, handler -> {
                        final Envelop response;
                        if (handler.succeeded()) {
                            /*
                             * // One Way message
                             * Only TRUE returned.
                             */
                            response = Envelop.success(Boolean.TRUE);
                        } else {
                            response = this.failure(address, handler);
                        }
                        Answer.reply(context, response, event);
                    });
                }
            });
        }, context, event), event);
    }
}
