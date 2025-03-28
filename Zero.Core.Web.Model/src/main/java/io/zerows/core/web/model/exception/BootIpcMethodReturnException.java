package io.zerows.core.web.model.exception;

import io.horizon.exception.BootingException;

import java.lang.reflect.Method;

public class BootIpcMethodReturnException extends BootingException {

    public BootIpcMethodReturnException(final Class<?> clazz,
                                        final Method method) {
        super(clazz, method);
    }

    @Override
    public int getCode() {
        return -40044;
    }
}
