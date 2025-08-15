package io.zerows.core.assembly.exception;

import io.horizon.exception.BootingException;

import java.lang.reflect.Method;

public class BootWorkerConflictException extends BootingException {

    public BootWorkerConflictException(final Class<?> clazz,
                                       final Method method) {
        super(clazz, method.getName(), method.getDeclaringClass());
    }

    @Override
    public int getCode() {
        return -40049;
    }
}
