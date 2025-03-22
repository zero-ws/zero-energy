package io.zerows.core.domain.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * Enum
 */
class EnumSaber extends AbstractSaber {

    @Override
    public <T> Object from(final T input) {
        return Fn.runOr(() -> {
            Object reference = null;
            if (input instanceof Enum) {
                reference = Ut.invoke(input, "name");
            }
            return reference;
        }, input);
    }
}
