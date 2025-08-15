package io.zerows.core.metadata.exception;

import io.horizon.eon.em.typed.EmType;
import io.horizon.exception.DaemonException;

public class DaemonDatatypeWrongException extends DaemonException {

    public DaemonDatatypeWrongException(final Class<?> clazz,
                                        final String field,
                                        final Object value,
                                        final EmType.Json type) {
        super(clazz, field, value, type);
    }

    @Override
    public int getCode() {
        return -10003;
    }
}
