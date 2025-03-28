package io.zerows.core.web.io.uca.response.resolver;

import io.horizon.uca.log.Annal;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.util.Ut;
import io.zerows.core.domain.uca.serialization.ZeroType;
import io.zerows.core.web.io.zdk.mime.Resolver;
import io.zerows.core.web.model.atom.Epsilon;

/**
 * Json Resolver
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class JsonResolver<T> implements Resolver<T> {

    private static final Annal LOGGER = Annal.get(JsonResolver.class);

    @Override
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income) {
        // Json Resolver
        final String content = context.body().asString();
        LOGGER.info(Ut.isNotNil(content), "( Resolver ) KIncome Type: {0}, Content = \u001b[0;37m{1}\u001b[m",
            income.getArgType().getName(), content);
        if (Ut.isNil(content)) {
            // Default Value set for BodyParam
            final T defaultValue = (T) income.getDefaultValue();
            income.setValue(defaultValue);
        } else {
            final Object result = ZeroType.value(income.getArgType(), content);
            if (null != result) {
                income.setValue((T) result);
            }
        }
        return income;
    }
}
