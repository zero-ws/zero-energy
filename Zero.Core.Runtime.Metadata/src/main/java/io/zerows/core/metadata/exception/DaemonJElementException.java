package io.zerows.core.metadata.exception;

import io.horizon.exception.DaemonException;

public class DaemonJElementException extends DaemonException {

    public DaemonJElementException(final Class<?> clazz,
                                   final int index,
                                   final Object value) {
        super(clazz, index, value);
    }

    @Override
    public int getCode() {
        return -10001;
    }
}
