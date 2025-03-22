package io.zerows.core.web.model.exception;

import io.horizon.exception.BootingException;

public class BootFilterContextException extends BootingException {

    public BootFilterContextException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -40051;
    }
}
