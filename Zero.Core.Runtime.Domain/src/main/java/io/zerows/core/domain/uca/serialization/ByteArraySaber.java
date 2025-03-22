package io.zerows.core.domain.uca.serialization;

import io.horizon.eon.VValue;
import io.vertx.up.fn.Fn;

class ByteArraySaber extends AbstractSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() -> Fn.runOr(Byte[].class == paramType ||
                    byte[].class == paramType, this.logger(),
                () -> literal.getBytes(VValue.DFT.CHARSET), () -> new byte[0]),
            paramType, literal);
    }
}
