package io.zerows.core.cloud.zdk.spi;

import io.horizon.atom.datamation.KDictSource;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public interface DictionaryPlugin {

    default DictionaryPlugin configuration(final JsonObject configuration) {
        return this;
    }

    Future<JsonArray> fetchAsync(KDictSource source, MultiMap paramMap);

    default JsonArray fetch(final KDictSource source, final MultiMap paramMap) {
        return new JsonArray();
    }
}
