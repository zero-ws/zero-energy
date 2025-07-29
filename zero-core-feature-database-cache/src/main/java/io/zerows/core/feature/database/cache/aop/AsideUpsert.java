package io.zerows.core.feature.database.cache.aop;

import io.vertx.core.Future;
import io.zerows.core.feature.database.cache.hit.CMessage;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Empty aspect for placeholder here
 */
@Aspect
@SuppressWarnings("all")
public class AsideUpsert extends L1AsideWriting {
/*
    @Before(value = "initialization(io.vertx.up.operation.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }
*/

    /*
     * upsert(id, Tool)
     *      <-- upsert(id, JsonObject)
     *      <-- upsert(id, JsonObject, pojo)
     *      <-- upsertJ(id, Tool)
     *      <-- upsertJ(id, JsonObject)
     *      <-- upsertJ(id, JsonObject, pojo)
     *
     * upsertAsync(id, Tool)
     *      <-- upsertAsync(id, JsonObject)
     *      <-- upsertAsync(id, JsonObject, pojo)
     *      <-- upsertJAsync(id, Tool)
     *      <-- upsertJAsync(id, JsonObject)
     *      <-- upsertJAsync(id, JsonObject, pojo)
     *
     * upsert(criteria, Tool)
     *      <-- upsert(criteria, JsonObject)
     *      <-- upsertJ(criteria, Tool)
     *      <-- upsertJ(criteria, JsonObject)
     *
     * upsert(criteria, Tool, pojo)
     *      <-- upsert(criteria, JsonObject, pojo)
     *      <-- upsertJ(criteria, Tool, pojo)
     *      <-- upsertJ(criteria, JsonObject, pojo)
     *
     * upsertAsync(criteria, Tool)
     *      <-- upsertAsync(criteria, JsonObject)
     *      <-- upsertJAsync(criteria, Tool)
     *      <-- upsertJAsync(criteria, JsonObject)
     *
     * upsertAsync(criteria, Tool, pojo)
     *      <-- upsertAsync(criteria, JsonObject, pojo)
     *      <-- upsertJAsync(criteria, Tool, pojo)
     *      <-- upsertJAsync(criteria, JsonObject, pojo)
     *
     * upsert(criteria, list, finder)
     *      <-- upsert(criteria, JsonArray, finder)
     *      <-- upsertJ(criteria, list, finder)
     *      <-- upsertJ(criteria, JsonArray, finder)
     *
     * upsert(criteria, list, finder, pojo)
     *      <-- upsert(criteria, JsonArray, finder, pojo)
     *      <-- upsertJ(criteria, list, finder, pojo)
     *      <-- upsertJ(criteria, JsonArray, finder, pojo)
     *
     * upsertAsync(criteria, list, finder)
     *      <-- upsertAsync(criteria, JsonArray, finder)
     *      <-- upsertJAsync(criteria, list, finder)
     *      <-- upsertJAsync(criteria, JsonArray, finder)
     *
     * upsertAsync(criteria, list, finder, pojo)
     *      <-- upsertAsync(criteria, JsonArray, finder, pojo)
     *      <-- upsertJAsync(criteria, list, finder, pojo)
     *      <-- upsertJAsync(criteria, JsonArray, finder, pojo)
     */
    /*
     * upsert(id, Tool)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsert(Object,T))")
    public <T> T upsert(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Tool
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(id, Tool)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsertAsync(Object,T))")
    public <T> Future<T> upsertAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<Tool>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsert(JsonObject, Tool)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsert(io.vertx.core.json.JsonObject, T))")
    public <T> T upsertByCond(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Tool
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(JsonObject, Tool)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsertAsync(io.vertx.core.json.JsonObject, T))")
    public <T> Future<T> upsertByCondAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<Tool>
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsert(JsonObject, Tool)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsert(io.vertx.core.json.JsonObject, T, String))")
    public <T> T upsertByPojo(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Tool
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(JsonObject, Tool)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsertAsync(io.vertx.core.json.JsonObject, T, String))")
    public <T> Future<T> upsertByPojoAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<Tool>
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }

    /*
     * upsert(JsonObject, Tool, BiPredicate)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsert(io.vertx.core.json.JsonObject, java.util.List, java.util.function.BiPredicate))")
    public <T> List<T> upsertList(final ProceedingJoinPoint point) throws Throwable {
        /*
         * List<Tool>
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(JsonObject, Tool, BiPredicate)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsertAsync(io.vertx.core.json.JsonObject, java.util.List, java.util.function.BiPredicate))")
    public <T> Future<List<T>> upsertListAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<List<Tool>>
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }


    /*
     * upsert(JsonObject, Tool, BiPredicate, String)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsert(io.vertx.core.json.JsonObject, java.util.List, java.util.function.BiPredicate, String))")
    public <T> List<T> upsertListFn(final ProceedingJoinPoint point) throws Throwable {
        /*
         * List<Tool>
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }

    /*
     * upsertAsync(JsonObject, Tool, BiPredicate,  String)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.upsertAsync(io.vertx.core.json.JsonObject, java.util.List, java.util.function.BiPredicate, String))")
    public <T> Future<List<T>> upsertListFnAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<List<Tool>>
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }
}
