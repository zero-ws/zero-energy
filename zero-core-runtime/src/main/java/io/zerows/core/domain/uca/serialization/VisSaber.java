package io.zerows.core.domain.uca.serialization;

import io.vertx.up.fn.Fn;
import io.zerows.core.domain.atom.commune.Vis;

class VisSaber extends AbstractSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() -> Vis.smart(literal), paramType, literal);
    }
}
