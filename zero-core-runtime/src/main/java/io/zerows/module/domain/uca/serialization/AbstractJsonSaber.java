package io.zerows.module.domain.uca.serialization;

import io.vertx.core.json.DecodeException;
import io.zerows.core.fn.Fn;
import io.zerows.module.domain.exception._400ParameterFromStringException;

import java.util.function.Function;

/**
 * InJson
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
