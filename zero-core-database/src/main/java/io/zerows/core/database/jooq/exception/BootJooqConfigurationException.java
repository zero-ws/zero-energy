package io.zerows.core.database.jooq.exception;

import io.horizon.exception.BootingException;

public class BootJooqConfigurationException extends BootingException {

    public BootJooqConfigurationException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40065;
    }
}
