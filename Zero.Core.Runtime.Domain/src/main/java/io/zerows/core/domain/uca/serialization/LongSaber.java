package io.zerows.core.domain.uca.serialization;

import java.util.function.Function;

/**
 * Long type
 */
@SuppressWarnings("unchecked")
class LongSaber extends AbstractNumericSaber {
    @Override
    protected boolean isValid(final Class<?> paramType) {
        return long.class == paramType || Long.class == paramType;
    }

    @Override
    protected Function<String, Long> getFun() {
        return Long::parseLong;
    }
}
