package io.zerows.core.domain.exception;

import io.horizon.exception.InternalException;

public class _AptParameterException extends InternalException {

    private static final String PATTERN = "The arguments could not be both null to build `Apt` component";

    public _AptParameterException(final Class<?> caller) {
        super(caller, PATTERN);
    }

    @Override
    public int getCode() {
        return -15000;
    }
}
