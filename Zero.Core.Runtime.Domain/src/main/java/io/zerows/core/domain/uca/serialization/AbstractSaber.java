package io.zerows.core.domain.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.domain.exception._400ParameterFromStringException;
import io.zerows.core.metadata.uca.logging.OLog;

public abstract class AbstractSaber implements Saber {

    protected OLog logger() {
        return Ut.Log.uca(this.getClass());
    }

    void verifyInput(final boolean condition,
                     final Class<?> paramType,
                     final String literal) {
        Fn.outWeb(condition,
            this.logger(), _400ParameterFromStringException.class,
            this.getClass(), paramType, literal);
    }

    @Override
    public <T> Object from(final T input) {
        // Default direct
        return input;
    }

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        // Default direct
        return literal;
    }
}
