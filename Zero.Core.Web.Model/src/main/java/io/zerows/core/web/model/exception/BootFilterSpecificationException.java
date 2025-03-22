package io.zerows.core.web.model.exception;

import io.horizon.exception.BootingException;

public class BootFilterSpecificationException extends BootingException {

    public BootFilterSpecificationException(final Class<?> clazz,
                                            final Class<?> filterCls) {
        super(clazz, filterCls);
    }

    @Override
    public int getCode() {
        return -40052;
    }
}
