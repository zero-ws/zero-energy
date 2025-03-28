package io.zerows.core.web.container.exception;

import io.horizon.exception.BootingException;

public class BootRpcEnvironmentException extends BootingException {

    public BootRpcEnvironmentException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40037;
    }
}
