package io.zerows.core.web.invocation.uca.parameter;

import io.horizon.atom.program.KRef;
import io.horizon.exception.web._501NotSupportException;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.util.Ut;
import io.zerows.core.web.model.commune.Envelop;

/**
 * 参数构造器，用于构造各种参数对应的相关对象，直接为上层调用提供依据，替换原来的 ToWeb
 *
 * @author lang : 2024-04-21
 */
public interface ParameterBuilder<SOURCE> {

    static ParameterBuilder<RoutingContext> ofAgent() {
        return ParameterAgent.of();
    }

    static ParameterBuilder<Envelop> ofWorker() {
        return ParameterWorker.of();
    }

    default Object build(final SOURCE envelop, final Class<?> type) {
        throw new _501NotSupportException(this.getClass());
    }

    default Object build(final SOURCE envelop, final Class<?> type, final KRef underway) {
        throw new _501NotSupportException(this.getClass());
    }
}

interface T {
    static boolean is(final Class<?> paramType, final Class<?> expected) {
        return expected == paramType || Ut.isImplement(paramType, expected);
    }
}