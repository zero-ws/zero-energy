package io.zerows.core.domain.uca.serialization;

import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

class CommonSaber extends AbstractSaber {
    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() ->
                Fn.runOr(!SaberTypes.isSupport(paramType), this.logger(),
                    // Turn On / Off
                    () -> Ut.deserialize(literal, paramType, true),
                    () -> null),
            paramType, literal);
    }

    @Override
    public <T> Object from(final T input) {
        return Fn.runOr(() -> {
            Object reference = null;
            if (!SaberTypes.isSupport(input.getClass())) {
                // final String literal = Ut.serialize(input);
                reference = Ut.serializeJson(input, true); // new JsonObject(literal);
            }
            return reference;
        }, input);
    }
}
