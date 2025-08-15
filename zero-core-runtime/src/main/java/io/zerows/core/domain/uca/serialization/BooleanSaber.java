package io.zerows.core.domain.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * Boolean
 */
class BooleanSaber extends AbstractSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(boolean.class == paramType || Boolean.class == paramType, this.logger(),
            () -> {

                this.verifyInput(!Ut.isBoolean(literal), paramType, literal);
                return Boolean.parseBoolean(literal);
            }, () -> Boolean.FALSE);
    }
}
