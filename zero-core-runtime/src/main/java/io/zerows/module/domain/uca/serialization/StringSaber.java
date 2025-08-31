package io.zerows.module.domain.uca.serialization;

import io.zerows.ams.constant.VString;
import io.zerows.core.fn.Fn;

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
