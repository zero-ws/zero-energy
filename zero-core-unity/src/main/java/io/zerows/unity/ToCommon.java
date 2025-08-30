package io.zerows.unity;

import io.zerows.core.exception.WebException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.zerows.core.fn.Fn;
import io.zerows.core.util.Ut;
import io.zerows.core.web.model.commune.Envelop;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("all")
class ToCommon {

    static JsonObject subset(final JsonObject input, final Set<String> removed) {
        removed.forEach(input::remove);
        return input;
    }

    static JsonArray subset(final JsonArray input, final Set<String> removed) {
        Ut.itJArray(input).forEach(json -> subset(json, removed));
        return input;
    }

    static <T> Future<T> future(final T entity) {
        return Fn.runOr(Future.succeededFuture(),
            () -> Fn.runOr(entity instanceof Throwable, null,
                () -> Future.failedFuture((Throwable) entity),
                () -> Future.succeededFuture(entity)),
            entity);
    }

    static <T> List<JsonObject> toJList(
        final List<T> list,
        final String pojo
    ) {
        return Fn.runOr(new ArrayList<>(), () -> {
            final List<JsonObject> jlist = new ArrayList<>();
            Ut.itJArray(Ut.toJson(list, pojo)).forEach(jlist::add);
            return jlist;
        }, list);
    }

    @SuppressWarnings("all")
    static <T> Envelop toEnvelop(
        final T entity
    ) {
        return Fn.runOr(Envelop.ok(), () -> Fn.runOr(entity instanceof WebException, null,
                () -> Envelop.failure((WebException) entity),
                () -> {
                    if (Envelop.class == entity.getClass()) {
                        return (Envelop) entity;
                    } else {
                        return Envelop.success(entity);
                    }
                }),
            entity);
    }

    static <T> Envelop toEnvelop(
        final T entity,
        final WebException error
    ) {
        return Fn.runOr(Envelop.failure(error),
            () -> Envelop.success(entity), entity);
    }

    static Envelop toEnvelop(
        final Class<? extends WebException> clazz,
        final Object... args
    ) {
        return Envelop.failure(Ut.failWeb(clazz, args));
    }

    static JsonObject toToggle(final Object... args) {
        final JsonObject params = new JsonObject();
        for (int idx = 0; idx < args.length; idx++) {
            final String idxStr = String.valueOf(idx);
            params.put(idxStr, args[idx]);
        }
        return params;
    }

    static <T, R> JsonObject toMerge(final T input, final String field, final List<R> list) {
        if (Objects.isNull(input)) {
            return new JsonObject();
        } else {
            final JsonObject serialized = Ut.serializeJson(input);
            if (Objects.nonNull(list)) {
                final JsonArray listData = Ut.serializeJson(list);
                serialized.put(field, listData);
            }
            return serialized;
        }
    }
}
