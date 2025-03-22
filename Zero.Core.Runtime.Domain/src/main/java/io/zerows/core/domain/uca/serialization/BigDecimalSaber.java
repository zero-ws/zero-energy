package io.zerows.core.domain.uca.serialization;

import io.vertx.up.fn.Fn;

import java.math.BigDecimal;
import java.util.function.Function;

/**
 * BigDecimal
 */
@SuppressWarnings("unchecked")
class BigDecimalSaber extends AbstractDecimalSaber {
    @Override
    protected boolean isValid(final Class<?> paramType) {
        return BigDecimal.class == paramType;
    }

    @Override
    protected Function<String, BigDecimal> getFun() {
        return BigDecimal::new;
    }

    @Override
    public <T> Object from(final T input) {
        return Fn.runOr(() -> {
            final BigDecimal decimal = (BigDecimal) input;
            return decimal.doubleValue();
        }, input);
    }
}
