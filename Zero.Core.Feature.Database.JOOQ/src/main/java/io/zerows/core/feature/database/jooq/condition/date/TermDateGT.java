package io.zerows.core.feature.database.jooq.condition.date;

import org.jooq.Condition;
import org.jooq.Field;
import org.jooq.impl.DSL;

import java.time.LocalDate;

@SuppressWarnings("all")
public class TermDateGT extends AbstractDateTerm {
    @Override
    public Condition where(final Field field, final String fieldName, final Object value) {
        return this.toDate(field, () -> {
            final LocalDate date = this.toDate(value);
            return DSL.field(fieldName).gt(date.atStartOfDay());
        }, () -> DSL.field(fieldName).gt(value));
    }
}
