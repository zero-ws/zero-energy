package io.zerows.core.metadata.exception;

import io.horizon.exception.BootingException;
import io.vertx.core.json.JsonObject;

public class BootDynamicKeyMissingException extends BootingException {

    public BootDynamicKeyMissingException(final Class<?> clazz,
                                          final String key,
                                          final JsonObject data) {
        super(clazz, key, data);
    }

    @Override
    public int getCode() {
        return -10005;
    }
}
