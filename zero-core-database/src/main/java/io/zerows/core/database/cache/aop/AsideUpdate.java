package io.zerows.core.database.cache.aop;

import io.vertx.core.Future;
import io.zerows.core.database.cache.hit.CMessage;
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
public class AsideUpdate extends L1AsideWriting {
/*    @Before(value = "initialization(io.vertx.up.operation.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }*/
    /*
     * update(Tool)
     *      <-- update(JsonObject)
     *      <-- update(JsonObject, pojo)
     *      <-- updateJ(Tool)
     *      <-- updateJ(JsonObject)
     *      <-- updateJ(JsonObject, pojo)
     *
     * updateAsync(Tool)
     *      <-- updateAsync(JsonObject)
     *      <-- updateAsync(JsonObject, pojo)
     *      <-- updateAsyncJ(Tool)
     *      <-- updateAsyncJ(JsonObject)
     *      <-- updateAsyncJ(JsonObject, pojo)
     *
     * update(List<Tool>)
     *      <-- update(JsonArray)
     *      <-- update(JsonArray, pojo)
     *      <-- updateJ(Tool)
     *      <-- updateJ(JsonArray)
     *      <-- updateJ(JsonArray, pojo)
     *
     * updateAsync(List<Tool>)
     *      <-- updateAsync(JsonArray)
     *      <-- updateAsync(JsonArray, pojo)
     *      <-- updateJAsync(List<Tool>)
     *      <-- updateJAsync(JsonArray)
     *      <-- updateJAsync(JsonArray, pojo)
     *
     * update(id, Tool)
     *      <-- update(id, JsonObject)
     *      <-- update(id, JsonObject, pojo)
     *      <-- updateJ(id, Tool)
     *      <-- updateJ(id, JsonObject)
     *      <-- updateJ(id, JsonObject, pojo)
     *
     * updateAsync(id, Tool)
     *      <-- updateAsync(id, JsonObject)
     *      <-- updateAsync(id, JsonObject, pojo)
     *      <-- updateJAsync(id, Tool)
     *      <-- updateJAsync(id, JsonObject)
     *      <-- updateJAsync(id, JsonObject, pojo)
     *
     * update(criteria, Tool)
     *      <-- update(criteria, JsonObject)
     *      <-- updateJ(criteria, Tool)
     *      <-- updateJ(criteria, JsonObject)
     *
     * update(criteria, Tool, pojo)
     *      <-- update(criteria, JsonObject, pojo)
     *      <-- updateJ(criteria, Tool, pojo)
     *      <-- updateJ(criteria, JsonObject, pojo)
     *
     * updateAsync(criteria, Tool)
     *      <-- updateAsync(criteria, JsonObject)
     *      <-- updateJAsync(criteria, Tool)
     *      <-- updateJAsync(criteria, JsonObject)
     *
     * updateAsync(criteria, Tool, pojo)
     *      <-- updateAsync(criteria, JsonObject, pojo)
     *      <-- updateJAsync(criteria, Tool, pojo)
     *      <-- updateJAsync(criteria, JsonObject, pojo)
     */

    /*
     * update(Tool)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.update(T))")
    public <T> T update(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Tool
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(Tool)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.updateAsync(T))")
    public <T> Future<T> updateAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<Tool>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * update(List<Tool>)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.update(java.util.List))")
    public <T> List<T> updateList(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  List<Tool>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(List<Tool>)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.updateAsync(java.util.List))")
    public <T> Future<List<T>> updateListAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<List<Tool>>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * update(id, Tool)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.update(Object, T))")
    public <T> T updateById(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Tool
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(id, Tool)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.updateAsync(Object, T))")
    public <T> Future<T> updateByIdAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<Tool>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * update(JsonObject, Tool)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.update(io.vertx.core.json.JsonObject, T))")
    public <T> T updateByCond(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Tool
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(JsonObject, Tool)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.updateAsync(io.vertx.core.json.JsonObject, T))")
    public <T> Future<T> updateByCondAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<Tool>
         */
        final List<CMessage> messages = this.messagesCond(point);
        return this.writeAsync(messages, point);
    }

    /*
     * update(JsonObject, Tool, String)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.update(io.vertx.core.json.JsonObject, T, String))")
    public <T> T updateByCondPojo(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Tool
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }

    /*
     * updateAsync(JsonObject, Tool, String)
     */
    @Around(value = "execution(* io.zerows.core.database.jooq.operation.UxJooq.updateAsync(io.vertx.core.json.JsonObject, T, String))")
    public <T> Future<T> updateByCondPojoAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         * Future<Tool>
         */
        final List<CMessage> messages = this.messagesPojo(point, 0);
        return this.writeAsync(messages, point);
    }
}
