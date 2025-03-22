package io.zerows.core.web.container.exception;

import io.horizon.exception.BootingException;

public class BootUpClassArgsException extends BootingException {

    public BootUpClassArgsException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40001;
    }
}
