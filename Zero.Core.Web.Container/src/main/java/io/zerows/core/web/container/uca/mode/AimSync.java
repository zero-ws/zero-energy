package io.zerows.core.web.container.uca.mode;

import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.fn.Fn;
import io.zerows.core.web.io.zdk.Aim;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.commune.Envelop;

/**
 * SyncAim: Non-Event Bus: Request-Response
 */
public class AimSync extends AbstractAim implements Aim<RoutingContext> {
    @Override
    public Handler<RoutingContext> attack(final Event event) {
        return Fn.runOr(() -> (context) -> this.exec(() -> {
            /*
             * Build arguments
             */
            final Object[] arguments = this.buildArgs(context, event);
            /*
             * Method callxx
             * Java reflector to call defined method.
             */
            try {
                final Object result = this.invoke(event, arguments);

                // 3. Resource model building
                // final Envelop data = Flower.continuous(context, result);
                /*
                 * Data handler to process Flower next result here.
                 */
                final Future<Envelop> future = Flower.next(context, result);
                future.onComplete(dataRes -> {
                    /*
                     * To avoid null dot result when the handler triggered result here
                     * SUCCESS
                     */
                    if (dataRes.succeeded()) {
                        /*
                         * Reply future result directly here.
                         */
                        Answer.reply(context, dataRes.result(), event);
                    }
                });
            } catch (final Throwable ex) {
                /*
                 * Reply error here
                 */
                final Envelop envelop = Envelop.failure(ex);
                Answer.reply(context, envelop);
            }

        }, context, event), event);
    }
}
