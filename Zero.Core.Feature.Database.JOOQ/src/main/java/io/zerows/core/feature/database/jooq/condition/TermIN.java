package io.zerows.core.feature.database.jooq.condition;

import io.vertx.up.util.Ut;
import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.util.Collection;

@SuppressWarnings("all")
public class TermIN implements Term {
    @Override
    public Condition where(final Field field, final String fieldName, final Object value) {
        final Collection<?> values = Ut.toCollection(value);
        return DSL.field(fieldName).in(values);
    }
}
