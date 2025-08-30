package io.zerows.module.domain.uca.serialization;

import io.zerows.agreed.constant.VValue;
import io.zerows.core.fn.Fn;

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
