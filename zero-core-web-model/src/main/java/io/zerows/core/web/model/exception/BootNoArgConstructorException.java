package io.zerows.core.web.model.exception;

import io.horizon.exception.BootingException;

public class BootNoArgConstructorException extends BootingException {

    public BootNoArgConstructorException(final Class<?> clazz,
                                         final Class<?> target) {
        super(clazz, target.getName());
    }

    @Override
    public int getCode() {
        return -40009;
    }
}
