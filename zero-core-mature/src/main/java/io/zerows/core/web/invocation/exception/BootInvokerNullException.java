package io.zerows.core.web.invocation.exception;

import io.horizon.exception.BootingException;

public class BootInvokerNullException extends BootingException {

    public BootInvokerNullException(final Class<?> target,
                                    final Class<?> returnType,
                                    final Class<?> paramType) {
        super(target, returnType, paramType);
    }

    @Override
    public int getCode() {
        return -40047;
    }
}
