package io.zerows.core.feature.database.jooq.exception;

import io.horizon.exception.BootingException;

public class BootJooqCondFieldException extends BootingException {

    public BootJooqCondFieldException(final Class<?> clazz,
                                      final String targetField) {
        super(clazz, targetField);
    }

    @Override
    public int getCode() {
        return -40055;
    }
}
