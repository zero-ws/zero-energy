package io.vertx.up.unity;

import io.horizon.eon.VString;
import io.modello.atom.app.KIntegration;
import io.modello.specification.HRecord;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.ext.auth.User;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.secure.AegisItem;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.database.DataPool;
import io.vertx.up.plugin.jooq.JooqDsl;
import io.vertx.up.plugin.jooq.JooqInfix;
import io.vertx.up.plugin.shared.UxPool;
import io.vertx.up.secure.Lee;
import io.vertx.up.secure.LeeBuiltIn;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * #「Kt」Utility X Component in zero
 * <p>
 * Here Ux is a util interface of uniform to call different tools.
 * It just like helper for income usage.
 */
@SuppressWarnings("all")
public final class Ux extends _Where {

    public static <T> Future<T> future(final T entity) {
        return ToCommon.future(entity);
    }

    public static <T> Future<T> future(final T input, final List<Function<T, Future<T>>> functions) {
        return Async.future(input, functions);
    }

    public static <T> Future<T> future(final T input, final Set<Function<T, Future<T>>> functions) {
        return Async.future(input, functions);
    }

    public static <T> Future<T> future() {
        return ToCommon.future(null);
    }

    public static <T> Handler<AsyncResult<T>> handler(final Message<Envelop> message) {
        return ToWeb.toHandler(message);
    }

    public static <T> Future<T> handler(final Consumer<Handler<AsyncResult<T>>> handler) {
        return ToWeb.toFuture(handler);
    }

    /*
     *  future prefix processing here
     *
     * JsonArray
     * 1) futureA
     * -- futureA()
     * -- futureA(List)
     * -- futureA(List, pojo)
     * -- futureA(String)
     * -- futureA(Record[])
     *
     * JsonObject
     * 2) futureJ
     * -- futureJ()
     * -- futureJ(T)
     * -- futureJ(T, pojo)
     * -- futureJ(String)
     * -- futureJ(Record)
     * -- futureJM(T, String)
     *
     * List
     * 3) futureL
     * -- futureL()
     * -- futureL(List)
     * -- futureL(List, pojo)
     * -- futureL(String)
     *
     * Grouped
     * 4) futureG
     * -- futureG(List, String)
     * -- futureG(T, String)
     * -- futureG(List)
     * -- futureG(T)
     *
     * Error Future
     * 5) futureE
     * -- futureE(T)
     * -- futureE(Supplier)
     *
     * New Api for normalized mount
     * 6) futureN
     * -- futureN(JsonObject, JsonObject)
     * -- futureN(JsonArray, JsonArray)
     * -- futureN(JsonArray, JsonArray, String)
     * > N for normalize and add new field:  __data,  __flag instead of original data
     *
     * Combine JsonObject and JsonArray by index
     * 7) futureC
     *
     * Filter JsonObject and JsonArray
     * 8) futureF
     * -- futureF(String...)
     * -- futureF(Set<String>)
     * -- futureF(ClustSerializble, String...)
     * -- futureF(JsonArray, String...)
     * -- futureF(JsonObject, Set<String>)
     * -- futureF(JsonArray, Set<String>)
     */
    // ----------------------- futureF ----------------------

    public static <T extends ClusterSerializable> Function<T, Future<T>> futureF(final Set<String> removed) {
        return input -> (input instanceof JsonObject) ?
            futureF((JsonObject) input, removed).compose(json -> ToCommon.future((T) json)) :
            futureF((JsonArray) input, removed).compose(array -> ToCommon.future((T) array));
    }

    public static <T extends ClusterSerializable> Function<T, Future<T>> futureF(final String... removed) {
        return futureF(Arrays.stream(removed).collect(Collectors.toSet()));
    }

    public static Future<JsonObject> futureF(final JsonObject input, final String... removed) {
        return ToCommon.future(ToCommon.subset(input, Arrays.stream(removed).collect(Collectors.toSet())));
    }

    public static Future<JsonObject> futureF(final JsonObject input, final Set<String> removed) {
        return ToCommon.future(ToCommon.subset(input, removed));
    }

    public static Future<JsonArray> futureF(final JsonArray input, final String... removed) {
        return ToCommon.future(ToCommon.subset(input, Arrays.stream(removed).collect(Collectors.toSet())));
    }

    public static Future<JsonArray> futureF(final JsonArray input, final Set<String> removed) {
        return ToCommon.future(ToCommon.subset(input, removed));
    }

    // ----------------------- futureN ----------------------
    public static Future<JsonObject> futureN(final JsonObject input, final JsonObject previous, final JsonObject current) {
        return ServiceNorm.effect(input, previous, current);
    }

    public static Future<JsonArray> futureN(final JsonArray previous, final JsonArray current) {
        return ServiceNorm.effect(previous, current, KName.KEY);
    }

    public static Future<JsonArray> futureN(final JsonArray previous, final JsonArray current, final String field) {
        return ServiceNorm.effect(previous, current, field);
    }

    public static <T> Future<T> futureC(final T input, final T processed) {
        return ServiceNorm.combine(input, processed);
    }

    public static Future<Boolean> futureT() {
        return ToCommon.future(Boolean.TRUE);
    }

    public static <T> Future<Boolean> futureT(final T input) {
        return ToCommon.future(Boolean.TRUE);
    }

    public static Future<Boolean> futureF() {
        return ToCommon.future(Boolean.FALSE);
    }

    public static <T> Future<List<T>> futureL() {
        return future(new ArrayList<>());
    }

    public static <T> Future<List<T>> futureL(final T single) {
        final List<T> list = new ArrayList<>();
        list.add(single);
        return future(list);
    }

    public static <T> Function<Throwable, Future<T>> futureE(final T input) {
        return Async.toErrorFuture(() -> input);
    }

    public static <T> Function<Throwable, Future<T>> futureE(final Supplier<T> supplier) {
        return Async.toErrorFuture(supplier);
    }

    public static <T> Future<JsonArray> futureA(final List<T> list, final String pojo) {
        return Future.succeededFuture(ToCommon.toJArray(list, pojo));
    }

    public static Future<JsonArray> futureA() {
        return futureA(new ArrayList<>(), VString.EMPTY);
    }

    public static Future<JsonArray> futureA(Throwable ex) {
        return Async.<JsonArray>toErrorFuture(JsonArray::new).apply(ex);
    }

    public static <T> Future<JsonArray> futureA(final List<T> list) {
        return futureA(list, VString.EMPTY);
    }

    public static <T> Function<List<T>, Future<JsonArray>> futureA(final String pojo) {
        return list -> futureA(list, pojo);
    }

    // --------------- T of entity processing -----------------

    public static <T> Future<JsonObject> futureJ(final T entity, final String pojo) {
        return Future.succeededFuture(ToCommon.toJObject(entity, pojo));
    }

    public static <T, R> Function<List<R>, Future<JsonObject>> futureJM(final T entity, final String field) {
        return list -> Future.succeededFuture(ToCommon.toMerge(entity, field, list));
    }

    public static Future<JsonObject> futureJ() {
        return futureJ(new JsonObject(), VString.EMPTY);
    }

    public static Future<JsonObject> futureJ(Throwable ex) {
        return Async.<JsonObject>toErrorFuture(JsonObject::new).apply(ex);
    }

    public static <T> Future<JsonObject> futureJ(final T entity) {
        return futureJ(entity, VString.EMPTY);
    }

    public static <T> Function<T, Future<JsonObject>> futureJ(final String pojo) {
        return entity -> futureJ(entity, pojo);
    }

    // --------------- List<T> of future processing -----------------
    public static <T> Future<List<JsonObject>> futureL(final List<T> list, final String pojo) {
        return Future.succeededFuture(ToCommon.toJList(list, pojo));
    }

    public static <T> Future<List<JsonObject>> futureLJ() {
        return futureL(new ArrayList<>(), VString.EMPTY);
    }

    public static <T> Future<List<JsonObject>> futureL(final List<T> list) {
        return futureL(list, VString.EMPTY);
    }

    public static <T> Function<List<T>, Future<List<JsonObject>>> futureL(final String pojo) {
        return list -> futureL(list, pojo);
    }

    // --------------- Record processing -----------------

    public static Future<JsonObject> futureJ(final HRecord record) {
        return Fn.runOr(futureJ(), () -> ToCommon.future(record.toJson()), record);
    }

    public static Future<JsonArray> futureA(final HRecord[] records) {
        return Fn.runOr(futureA(), () -> ToCommon.future(Ut.toJArray(records)), records);
    }

    // --------------- Future of Map -----------------
    public static <T> Future<ConcurrentMap<String, JsonArray>> futureG(final List<T> item, final String field) {
        return futureG(ToCommon.toJArray(item, ""), field);
    }

    public static Future<ConcurrentMap<String, JsonArray>> futureG(final JsonArray item, final String field) {
        return Future.succeededFuture(Ut.elementGroup(item, field));
    }

    public static <T> Future<ConcurrentMap<String, JsonArray>> futureG(final List<T> item) {
        return futureG(ToCommon.toJArray(item, ""), "type");
    }

    public static Future<ConcurrentMap<String, JsonArray>> futureG(final JsonArray item) {
        return futureG(item, "type");
    }

    /*
     * key part for extract data from environment
     */
    public static String keyUser(final User user) {
        Objects.requireNonNull(user);
        final JsonObject principle = user.principal();
        if (principle.containsKey(KName.USER)) {
            return principle.getString(KName.USER);
        } else {
            /*
             * To avoid fetch user information from JWT token
             */
            final String accessToken = principle.getString(KName.ACCESS_TOKEN);
            final JsonObject credential = Jwt.extract(accessToken);
            return credential.getString(KName.USER);
        }
    }

    // ---------------------------------- Children Utility

    /**
     * Inner class of `Jooq` tool of Jooq Engine operations based on pojo here.
     * When developers want to access database and select zero default implementation.
     *
     * @author lang
     * <pre><code>
     *
     *     public Future<JsonObject> fetchAsync(final User user){
     *         return Ux.Jooq.on(UserDao.class)
     *                    .insertAsync(user)
     *                    .compose(Ux::fnJObject);
     *     }
     *
     * </code></pre>
     * <p>
     * Here you can do database access smartly and do nothing then.
     */
    public static class Jooq {

        /**
         * Get reference of UxJooq that bind to Dao class, this method won't access standard database,
         * instead it will access history database that has been configured in `vertx-jooq.yml` file.
         * <p>
         * key = orbit
         *
         * <pre><code>
         *
         * jooq:
         *   orbit:
         *     driverClassName: "com.mysql.cj.jdbc.Driver"
         *     ......
         *
         * </code></pre>
         *
         * @param clazz The class of `VertxDao` that has been generated by jooq tool
         *
         * @return UxJooq reference that has been initialized
         */
        public static UxJooq ons(final Class<?> clazz) {
            final JooqDsl dsl = JooqInfix.getDao(clazz, YmlCore.jooq.ORBIT);
            return CACHE.CC_JOOQ.pick(() -> new UxJooq(clazz, dsl), "HIS-" + dsl.poolKey());
            // return Fn.po?lThread(Cache.JOOQ_POOL_HIS, () -> new UxJooq(clazz, dsl), dsl.poolKey());
        }

        /**
         * Get reference of UxJooq that bind to Dao class, this method access standard database,
         * the configured position is `vertx-jooq.yml`
         * <p>
         * key = provider
         *
         * <pre><code>
         * jooq:
         *   provider:
         *     driverClassName: "com.mysql.cj.jdbc.Driver"
         * </code></pre>
         *
         * @param clazz The class of `VertxDao` that has been generated by jooq tool
         *
         * @return UxJooq reference that has been initialized
         */
        public static UxJooq on(final Class<?> clazz) {
            final JooqDsl dsl = JooqInfix.getDao(clazz);
            return CACHE.CC_JOOQ.pick(() -> new UxJooq(clazz, dsl), dsl.poolKey());
            // return Fn.po?lThread(Cache.JOOQ_POOL, () -> new UxJooq(clazz, dsl), dsl.poolKey());
        }

        /**
         * The overloading method of above `on(Class<?>)` method here.
         *
         * @param clazz The class of `VertxDao` that has been generated by jooq tool
         * @param pool  Input data pool reference, it provide developers to access other database in one application.
         *
         * @return UxJooq reference that has been initialized
         */
        public static UxJooq on(final Class<?> clazz, final DataPool pool) {
            final JooqDsl dsl = JooqInfix.getDao(clazz, pool);
            return CACHE.CC_JOOQ.pick(() -> new UxJooq(clazz, dsl), dsl.poolKey());
            // return Fn.po?lThread(Cache.JOOQ_POOL, () -> new UxJooq(clazz, dsl), dsl.poolKey());
        }

        /**
         * The overloading method of above `on(Class<?>)` method here.
         *
         * @param clazz The class of `VertxDao` that has been generated by jooq tool
         * @param key   the key configuration in vertx-jooq.yml such as above "orbit", "provider"
         *
         * @return UxJooq reference that has been initialized
         */
        public static UxJooq on(final Class<?> clazz, final String key) {
            final JooqDsl dsl = JooqInfix.getDao(clazz, key);
            return CACHE.CC_JOOQ.pick(() -> new UxJooq(clazz, dsl), dsl.poolKey());
            // return Fn.po?lThread(Cache.JOOQ_POOL, () -> new UxJooq(clazz, dsl), dsl.poolKey());
        }
    }

    // -> Jooq -> Multi
    public static class Join {

        public static UxJoin on(final String configFile) {
            return new UxJoin(configFile);
        }

        public static UxJoin on() {
            return new UxJoin(null);
        }

        public static UxJoin on(final Class<?> daoCls) {
            return new UxJoin(null).add(daoCls);
        }
    }

    public static class Pool {

        public static UxPool on(final String name) {
            return CACHE.CC_UX_POOL.pick(() -> new UxPool(name), name);
            // return Fn.po?l(Cache.MAP_POOL, name, () -> new UxPool(name));
        }

        public static UxPool on() {
            return new UxPool();
        }
    }

    public static class Job {
        public static UxJob on() {
            return new UxJob();
        }
    }

    public static class Ldap {
        public static UxLdap on(final KIntegration integration) {
            return CACHE.CC_LDAP.pick(() -> new UxLdap(integration), String.valueOf(integration.hashCode()));
        }
    }

    /*
     * Here the Jwt class is for lagency system because all following method will be called and existed
     * But the new structure is just like following:
     *
     * 1. Lee -> ( Impl ) by Service Loader
     * 2. The `AuthWall.JWT` will be selected and called API of Lee interface
     * 3. The final result is token ( encoding / decoding ) part
     * 4. The implementation class is defined in `zero-ifx-auth` instead of standard framework
     *
     * If you want to use security module, you should set-up `zero-ifx-auth` infix instead, or
     * you can run zero framework in non-secure mode
     */
    public static class Jwt {

        public static String token(final JsonObject data) {
            final Lee lee = Ut.service(LeeBuiltIn.class);
            return lee.encode(data, AegisItem.configMap(EmSecure.AuthWall.JWT));
        }

        public static JsonObject extract(final String token) {
            final Lee lee = Ut.service(LeeBuiltIn.class);
            return lee.decode(token, AegisItem.configMap(EmSecure.AuthWall.JWT));
        }

        public static JsonObject extract(final JsonObject jwtToken) {
            return extract(jwtToken.getString(KName.ACCESS_TOKEN));
        }
    }

}
