package io.zerows.core.web.validation.uca.rules;

import io.horizon.exception.WebException;
import io.zerows.core.web.model.atom.Rule;

import java.util.Collection;

public interface Ruler {
    static Ruler get(final String type) {
        return CACHE.RULERS.get(type);
    }

    static WebException verify(final Collection<Rule> rules,
                               final String field,
                               final Object value) {
        WebException error = null;
        for (final Rule rule : rules) {
            final Ruler ruler = get(rule.getType());
            if (null != ruler) {
                error = ruler.verify(field, value, rule);
            }
            // Error found
            if (null != error) {
                break;
            }
        }
        return error;
    }

    /**
     * Verify each field for @BodyParam
     *
     * @param field Input field of the data structure
     * @param value The input field reflect value literal
     * @param rule  The rule that has been defined.
     *
     * @return WebException that the validated error here.
     */
    WebException verify(final String field,
                        final Object value,
                        final Rule rule);
}
