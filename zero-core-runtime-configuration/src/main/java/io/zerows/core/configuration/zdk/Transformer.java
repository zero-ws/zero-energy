package io.zerows.core.configuration.zdk;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.logging.OLog;

public interface Transformer<T> {
    T transform(JsonObject input);

    default OLog logger() {
        return Ut.Log.configure(this.getClass());
    }
}
