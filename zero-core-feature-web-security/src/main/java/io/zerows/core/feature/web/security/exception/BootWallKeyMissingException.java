package io.zerows.core.feature.web.security.exception;

import io.horizon.exception.BootingException;

public class BootWallKeyMissingException extends BootingException {

    public BootWallKeyMissingException(final Class<?> clazz,
                                       final String key,
                                       final Class<?> target) {
        super(clazz, key, target);
    }

    @Override
    public int getCode() {
        return -40040;
    }
}
