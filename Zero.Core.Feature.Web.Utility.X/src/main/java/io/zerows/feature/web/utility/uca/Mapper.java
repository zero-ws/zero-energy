package io.zerows.feature.web.utility.uca;

import io.horizon.atom.datamation.KMapping;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

/*
 * Dual Processing for
 * ActIn / ActOut
 */
public interface Mapper {
    /*
     * Mapping
     * to -> from
     */
    JsonObject in(JsonObject in, KMapping mapping);

    default JsonArray in(final JsonArray in, final KMapping mapping) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(in).map(each -> this.in(each, mapping)).forEach(normalized::add);
        return normalized;
    }

    /*
     * Mapping
     * from -> to
     */
    JsonObject out(JsonObject out, KMapping mapping);

    default JsonArray out(final JsonArray out, final KMapping mapping) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(out).map(each -> this.out(each, mapping)).forEach(normalized::add);
        return normalized;
    }
}
