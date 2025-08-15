package io.zerows.core.web.model.exception;

import io.horizon.exception.BootingException;

public class BootFilterOrderException extends BootingException {

    public BootFilterOrderException(final Class<?> clazz,
                                    final Class<?> filterCls) {
        super(clazz, filterCls);
    }

    @Override
    public int getCode() {
        return -40053;
    }
}
