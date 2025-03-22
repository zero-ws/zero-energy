package io.zerows.core.domain.uca.serialization;

import io.vertx.core.json.DecodeException;
import io.vertx.up.fn.Fn;
import io.zerows.core.domain.exception._400ParameterFromStringException;

import java.util.function.Function;

/**
 * Json
 */
public abstract class AbstractJsonSaber extends AbstractSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() -> Fn.runOr(this.isValid(paramType), this.logger(),
                () -> {
                    try {
                        return this.getFun().apply(literal);
                    } catch (final DecodeException ex) {
                        // Do not do anything
                        // getLogger().jvm(ex);
                        throw new _400ParameterFromStringException(this.getClass(), paramType, literal);
                    }
                }, () -> null),
            paramType, literal);
    }

    protected abstract boolean isValid(final Class<?> paramType);

    protected abstract <T> Function<String, T> getFun();
}
