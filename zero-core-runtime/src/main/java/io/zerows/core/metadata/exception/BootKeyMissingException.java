package io.zerows.core.metadata.exception;

import io.horizon.exception.BootingException;

public class BootKeyMissingException extends BootingException {

    public BootKeyMissingException(final Class<?> clazz,
                                   final String key) {
        super(clazz, key);
    }

    @Override
    public int getCode() {
        return -40020;
    }
}
