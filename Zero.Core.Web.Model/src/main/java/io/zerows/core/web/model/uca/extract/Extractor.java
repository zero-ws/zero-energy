package io.zerows.core.web.model.uca.extract;

import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.logging.OLog;

public interface Extractor<T> {

    T extract(Class<?> clazz);

    default OLog logger() {
        return Ut.Log.uca(this.getClass());
    }
}
