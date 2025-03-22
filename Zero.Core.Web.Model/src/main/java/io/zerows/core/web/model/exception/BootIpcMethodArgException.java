package io.zerows.core.web.model.exception;

import io.horizon.exception.BootingException;

import java.lang.reflect.Method;

public class BootIpcMethodArgException extends BootingException {

    public BootIpcMethodArgException(final Class<?> clazz,
                                     final Method method) {
        super(clazz, method);
    }

    @Override
    public int getCode() {
        return -40046;
    }
}
