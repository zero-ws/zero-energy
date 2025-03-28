package io.zerows.core.feature.web.security.exception;

import io.horizon.exception.BootingException;

public class BootWallProviderConflictException extends BootingException {

    public BootWallProviderConflictException(final Class<?> clazz,
                                             final Class<?> target) {
        super(clazz, target);
    }

    @Override
    public int getCode() {
        return -40077;
    }
}
