package io.zerows.core.domain.uca.serialization;

import io.horizon.eon.VString;
import io.vertx.up.fn.Fn;

/**
 * StringBuffer, StringBuilder
 */
class StringBufferSaber extends AbstractSaber {
    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() ->
                Fn.runOr(StringBuilder.class == paramType
                        || StringBuffer.class == paramType, this.logger(),
                    () -> {
                        if (StringBuffer.class == paramType) {
                            return new StringBuffer(literal);
                        } else {
                            return new StringBuilder(literal);
                        }
                    }, () -> VString.EMPTY),
            paramType, literal);
    }

    @Override
    public <T> Object from(final T input) {
        return Fn.runOr(() -> {
            Object reference = null;
            if (input instanceof StringBuilder
                || input instanceof StringBuffer) {
                reference = input.toString();
            }
            return reference;
        }, input);
    }
}
