package io.zerows.core.metadata.exception;

import io.horizon.exception.DaemonException;
import io.vertx.core.json.JsonObject;

public class DaemonFieldRequiredException extends DaemonException {

    public DaemonFieldRequiredException(final Class<?> clazz,
                                        final JsonObject data,
                                        final String field) {
        super(clazz, data.encode(), field);
    }

    @Override
    public int getCode() {
        return -10002;
    }
}
