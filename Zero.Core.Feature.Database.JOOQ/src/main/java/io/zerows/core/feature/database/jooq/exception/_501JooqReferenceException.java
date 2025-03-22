package io.zerows.core.feature.database.jooq.exception;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;

public class _501JooqReferenceException extends WebException {

    public _501JooqReferenceException(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public int getCode() {
        return -80217;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.NOT_IMPLEMENTED;
    }
}
