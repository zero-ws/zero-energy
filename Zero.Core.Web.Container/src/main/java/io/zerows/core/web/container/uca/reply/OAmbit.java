package io.zerows.core.web.container.uca.reply;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.util.Ut;
import io.zerows.core.web.model.commune.Envelop;

/**
 * AOP 模式下的处理流程，前置和后置都带特定的处理流程，以保证 AOP 的完整性
 *
 * @author lang : 2024-06-27
 */
public interface OAmbit {

    Cc<String, OAmbit> CC_SKELETON = Cc.openThread();

    static OAmbit of(final Class<?> ambitCls) {
        return CC_SKELETON.pick(() -> Ut.instance(ambitCls), ambitCls.getName());
    }

    Future<Envelop> then(RoutingContext context, Envelop envelop);
}
