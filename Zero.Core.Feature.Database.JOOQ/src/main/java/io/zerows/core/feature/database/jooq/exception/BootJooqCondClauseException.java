package io.zerows.core.feature.database.jooq.exception;

import io.horizon.exception.BootingException;

public class BootJooqCondClauseException extends BootingException {
    public BootJooqCondClauseException(final Class<?> clazz, final String field,
                                       final Class<?> type, final String original) {
        super(clazz, field, type, original);
    }

    @Override
    public int getCode() {
        return -40067;
    }
}
