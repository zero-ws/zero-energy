package io.zerows.core.domain.uca.serialization;

import io.horizon.eon.VString;
import io.vertx.up.fn.Fn;

/**
 * String
 */
class StringSaber extends AbstractSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() ->
                Fn.runOr(String.class == paramType, this.logger(),
                    () -> literal, () -> VString.EMPTY),
            paramType, literal);
    }
}
