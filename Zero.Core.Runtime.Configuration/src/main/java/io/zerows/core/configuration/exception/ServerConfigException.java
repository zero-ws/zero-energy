package io.zerows.core.configuration.exception;

import io.horizon.exception.BootingException;

/**
 * Server config:
 * server:
 * -
 */
public class ServerConfigException extends BootingException {
    public ServerConfigException(final Class<?> clazz,
                                 final String config) {
        super(clazz, config);
    }

    @Override
    public int getCode() {
        return -30001;
    }
}
