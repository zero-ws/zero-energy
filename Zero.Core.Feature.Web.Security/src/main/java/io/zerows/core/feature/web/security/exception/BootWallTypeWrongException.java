package io.zerows.core.feature.web.security.exception;

import io.horizon.exception.BootingException;

public class BootWallTypeWrongException extends BootingException {

    public BootWallTypeWrongException(final Class<?> clazz,
                                      final String key,
                                      final Class<?> target) {
        super(clazz, key, target);
    }

    @Override
    public int getCode() {
        return -40075;
    }
}
