package io.zerows.core.feature.database.jooq.condition;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

@SuppressWarnings("all")
public class TermLT implements Term {
    @Override
    public Condition where(final Field field, final String fieldName, final Object value) {
        return DSL.field(fieldName).lt(value);
    }
}
