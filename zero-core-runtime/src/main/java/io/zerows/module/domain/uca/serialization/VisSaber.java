package io.zerows.module.domain.uca.serialization;

import io.zerows.core.fn.Fn;
import io.zerows.module.domain.atom.commune.Vis;

class VisSaber extends AbstractSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() -> Vis.smart(literal), paramType, literal);
    }
}
