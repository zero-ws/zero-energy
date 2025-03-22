package io.vertx.up.util;

import io.reactivex.rxjava3.core.Observable;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.zerows.core.metadata.atom.mapping.Mirror;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author lang : 2024-04-19
 */
class Json {
    static <T> JsonObject toJObject(
        final T entity,
        final String pojo) {
        return Fn.runOr(new JsonObject(),
            () -> Fn.runOr(Ut.isNil(pojo), null,
                // Turn On Smart
                () -> Ut.serializeJson(entity, true),
                () -> Mirror.create(Json.class)
                    .mount(pojo)
                    .connect(Ut.serializeJson(entity, true))
                    .to()
                    .result()),
            entity);
    }

    static <T> JsonArray toJArray(
        final List<T> list,
        final String pojo
    ) {
        return Fn.runOr(new JsonArray(), () -> {
            final JsonArray array = new JsonArray();
            Observable.fromIterable(list)
                .filter(Objects::nonNull)
                .map(item -> toJObject(item, pojo))
                .subscribe(array::add).dispose();
            return array;
        }, list);
    }

    static <T> T from(final JsonObject data, final Class<T> clazz,
                      final String pojo) {
        return Fn.runOr(Ut.isNil(pojo), null,
            // Turn On Smart Serialization on Business Layer
            () -> Ut.deserialize(data, clazz, true),
            () -> Mirror.create(Json.class)
                .mount(pojo)
                .connect(data)
                .type(clazz)
                .from()
                .get());
    }

    @SuppressWarnings("all")
    static <T> List<T> from(final JsonArray data, final Class<?> clazz, final String pojo) {
        final List<T> result = new ArrayList<>();
        Ut.itJArray(data).map(each -> from(each, clazz, pojo))
            .filter(Objects::nonNull)
            .map(item -> (T) item)
            .forEach(result::add);
        return result;
    }
}
