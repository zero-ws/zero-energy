package io.zerows.core.metadata.exception;

import io.horizon.exception.DaemonException;
import io.vertx.core.json.JsonObject;

public class DaemonFieldWrongException extends DaemonException {

    public DaemonFieldWrongException(final Class<?> clazz,
                                     final JsonObject data,
                                     final String field) {
        super(clazz, data.encode(), field);
    }

    @Override
    public int getCode() {
        return -10006;
    }
}
