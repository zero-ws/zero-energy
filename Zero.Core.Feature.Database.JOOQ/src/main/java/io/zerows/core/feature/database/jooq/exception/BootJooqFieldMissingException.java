package io.zerows.core.feature.database.jooq.exception;

import io.horizon.exception.BootingException;

public class BootJooqFieldMissingException extends BootingException {

    public BootJooqFieldMissingException(
        final Class<?> clazz,
        final String field,
        final Class<?> type) {
        super(clazz, field, type.getName());
    }

    @Override
    public int getCode() {
        return -40059;
    }
}
