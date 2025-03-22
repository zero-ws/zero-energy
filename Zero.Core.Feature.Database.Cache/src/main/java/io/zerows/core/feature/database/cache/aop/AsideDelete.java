package io.zerows.core.feature.database.cache.aop;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
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
public class AsideDelete extends L1AsideWriting {
/*    @Before(value = "initialization(io.vertx.up.operation.jooq.UxJooq.new(..)) && args(clazz,dao)", argNames = "clazz,dao")
    public void init(final Class<?> clazz, final VertxDAO dao) {
        super.initialize(clazz, dao);
    }*/
    /*
     * delete(Tool)
     *      <-- delete(JsonObject)
     *      <-- delete(JsonObject, pojo)
     *      <-- deleteJ(Tool)
     *      <-- deleteJ(JsonObject)
     *      <-- deleteJ(JsonObject, pojo)
     *
     * deleteAsync(Tool)
     *      <-- deleteAsync(JsonObject)
     *      <-- deleteAsync(JsonObject, pojo)
     *      <-- deleteJAsync(Tool)
     *      <-- deleteJAsync(JsonObject)
     *      <-- deleteJAsync(JsonObject, pojo)
     *
     * delete(List<Tool>)
     *      <-- delete(JsonArray)
     *      <-- delete(JsonArray, pojo)
     *      <-- deleteJ(List<Tool>)
     *      <-- deleteJ(JsonArray)
     *      <-- deleteJ(JsonArray, pojo)
     *
     * deleteAsync(List<Tool>)
     *      <-- deleteAsync(JsonArray)
     *      <-- deleteAsync(JsonArray, pojo)
     *      <-- deleteJAsync(List<Tool>)
     *      <-- deleteJAsync(JsonArray)
     *      <-- deleteJAsync(JsonArray, pojo)
     *
     * deleteById(ID...)
     * deleteById(Collection<ID> ids)
     * deleteByIdAsync(ID...)
     * deleteByIdAsync(Collection<ID> ids)
     *
     * deleteBy(JsonObject)
     * deleteBy(JsonObject, pojo)
     * deleteByAsync(JsonObject)
     * deleteByAsync(JsonObject, pojo)
     */

    /*
     * deleteById
     * deleteByIdAsync
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.deleteById*(..)) && args(id)", argNames = "id")
    public <T> T deleteById(final ProceedingJoinPoint point, final Object id) throws Throwable {
        /*
         * Object[] / Collection
         */
        final List<CMessage> messages = this.messages(id, point);
        return this.writeAsync(messages, point);
    }

    /*
     * deleteBy
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.deleteBy(..))")
    public Boolean deleteBy(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * Get all ids
             * deleteBy(JsonObject)
             */
            final List<CMessage> messages = this.messagesCond(point);
            return this.writeAsync(messages, point);
        } else {
            /*
             * Pojo mode ignored
             * deleteBy(JsonObject, pojo)
             */
            final List<CMessage> messages = this.messagesPojo(point, 0);
            return this.writeAsync(messages, point);
        }
    }

    /*
     * deleteByAsync
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.deleteByAsync(..))")
    public Future<Boolean> deleteByAsync(final ProceedingJoinPoint point) throws Throwable {
        if (L1Analyzer.isMatch(point, JsonObject.class)) {
            /*
             * Get all ids
             * deleteByAsync(JsonObject)
             */
            final List<CMessage> messages = this.messagesCond(point);
            return this.writeAsync(messages, point);
        } else {
            /*
             * Pojo mode ignored
             * deleteByAsync(JsonObject, pojo)
             */
            final List<CMessage> messages = this.messagesPojo(point, 0);
            return this.writeAsync(messages, point);
        }
    }

    /*
     * delete(Tool)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.delete(T))")
    public <T> T delete(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Tool
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * delete(List<Tool>)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.delete(java.util.List))")
    public <T> List<T> deleteList(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  List<Tool>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * deleteAsync(Tool)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.deleteAsync(T))")
    public <T> Future<T> deleteAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<Tool>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }

    /*
     * deleteAsync(List<Tool>)
     */
    @Around(value = "execution(* io.zerows.core.feature.database.jooq.operation.UxJooq.deleteAsync(java.util.List))")
    public <T> Future<List<T>> deleteListAsync(final ProceedingJoinPoint point) throws Throwable {
        /*
         *  Future<List<Tool>>
         */
        final List<CMessage> messages = this.messagesT(point);
        return this.writeAsync(messages, point);
    }
}
