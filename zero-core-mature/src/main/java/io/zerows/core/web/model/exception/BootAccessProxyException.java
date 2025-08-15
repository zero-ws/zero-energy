package io.zerows.core.web.model.exception;

import io.horizon.exception.BootingException;

public class BootAccessProxyException extends BootingException {

    public BootAccessProxyException(final Class<?> clazz,
                                    final Class<?> target) {
        super(clazz, target.getName());
    }

    @Override
    public int getCode() {
        return -40010;
    }
}
