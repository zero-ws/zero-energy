package io.zerows.core.web.io.uca.request.argument;

import io.vertx.ext.web.RoutingContext;
import io.zerows.core.fn.Fn;
import io.zerows.module.domain.uca.serialization.ZeroType;

import java.util.Map;

public class ContextFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        final Map<String, Object> data = context.data();
        final Object value = data.get(name);
        return Fn.runOr(() -> {
            if (paramType == value.getClass()) {
                return value;
            } else {
                final String valueStr = value.toString();
                return ZeroType.value(paramType, valueStr);
            }
        }, value);
    }
}
