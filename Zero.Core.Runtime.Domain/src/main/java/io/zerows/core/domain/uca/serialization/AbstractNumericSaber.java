package io.zerows.core.domain.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.function.Function;

/**
 * Int, Long, Short
 */
public abstract class AbstractNumericSaber extends AbstractSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() ->
                Fn.runOr(this.isValid(paramType), this.logger(),
                    () -> {
                        this.verifyInput(!Ut.isInteger(literal), paramType, literal);
                        return this.getFun().apply(literal);
                    }, () -> null),
            paramType, literal);
    }

    protected abstract boolean isValid(final Class<?> paramType);

    protected abstract <T> Function<String, T> getFun();
}
