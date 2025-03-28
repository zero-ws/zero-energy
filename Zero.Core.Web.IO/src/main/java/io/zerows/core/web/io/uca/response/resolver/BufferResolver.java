package io.zerows.core.web.io.uca.response.resolver;

import io.horizon.exception.WebException;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.zerows.core.web.io.zdk.mime.Resolver;
import io.zerows.core.web.model.atom.Epsilon;

public class BufferResolver<T> implements Resolver<T> {

    @Override
    @SuppressWarnings("all")
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income)
        throws WebException {
        final Class<?> clazz = income.getArgType();
        if (Buffer.class == clazz) {
            final Buffer body = context.body().buffer();
            income.setValue((T) body);
        }
        return income;
    }
}
