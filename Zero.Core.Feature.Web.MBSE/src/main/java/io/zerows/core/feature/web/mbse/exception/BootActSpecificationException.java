package io.zerows.core.feature.web.mbse.exception;

import io.horizon.exception.BootingException;

public class BootActSpecificationException extends BootingException {

    public BootActSpecificationException(final Class<?> clazz,
                                         final Boolean isBatch) {
        super(clazz, isBatch);
    }

    @Override
    public int getCode() {
        return -40064;
    }
}
