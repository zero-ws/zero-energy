package io.zerows.core.web.model.commune;

import io.horizon.exception.WebException;
import io.horizon.exception.web._500InternalServerException;
import io.modello.eon.em.EmValue;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import io.zerows.core.security.zdk.authority.Acl;
import io.zerows.core.web.model.exception._000HttpWebException;

import java.util.Objects;

/**
 * @author lang : 2024-04-21
 */
class Action {
    /** ACL */
    static void outAcl(final Acl acl, final JsonObject dataRef) {
        if (Objects.isNull(dataRef) || Objects.isNull(acl)) {
            return;
        }
        final JsonObject aclData = acl.acl();
        if (Ut.isNotNull(aclData)) {
            dataRef.put(KName.Rbac.ACL, aclData);
        }
    }

    /** Me */
    static void inMe(final Envelop envelopRef, final EmValue.Bool active, final boolean app) {
        final JsonObject headerX = envelopRef.headersX();
        envelopRef.value(KName.SIGMA, headerX.getValue(KName.SIGMA));
        if (EmValue.Bool.IGNORE != active) {
            envelopRef.value(KName.ACTIVE, EmValue.Bool.TRUE == active ? Boolean.TRUE : Boolean.FALSE);
        }
        // this.value(KName.ACTIVE, active);
        if (headerX.containsKey(KName.LANGUAGE)) {
            envelopRef.value(KName.LANGUAGE, headerX.getValue(KName.LANGUAGE));
        }
        if (app) {
            envelopRef.value(KName.APP_ID, headerX.getValue(KName.APP_ID));
            envelopRef.value(KName.APP_KEY, headerX.getValue(KName.APP_KEY));
        }
    }

    /** Response **/
    static <T> Envelop outNext(final T entity) {
        // 为空时的基本操作（保证 200）
        if (Objects.isNull(entity)) {
            return Envelop.ok();
        }
        // 执行非空的判断
        if (entity instanceof final WebException failure) {
            return Envelop.failure(failure);
        } else if (entity instanceof final Envelop result) {
            // 有值防止过度封装
            return result;
        } else {
            return Envelop.success(entity);
        }
    }

    /** Exception **/
    static Envelop outFailure(final Throwable ex) {
        if (ex instanceof WebException) {
            // Throwable converted to WebException
            return Envelop.failure((WebException) ex);
        } else {
            if (ex instanceof HttpException) {
                // Http Exception, When this situation, the ex may contain WebException internal
                final Throwable actual = ex.getCause();
                if (Objects.isNull(actual)) {
                    // No Cause
                    return Envelop.failure(new _000HttpWebException(Envelop.class, (HttpException) ex));
                } else {
                    /*
                     * 1. Loop to search until `WebException`
                     * 2. Or HttpException without cause trace
                     */
                    return Envelop.failure(actual);
                }
            } else {
                // Common JVM Exception
                return Envelop.failure(new _500InternalServerException(Envelop.class, ex.getMessage()));
            }
        }
    }

    static void copyFrom(final Assist assistRef, final RoutingContext context) {
        /* Bind Context for Session / User etc. */
        assistRef.bind(context);
        final HttpServerRequest request = context.request();

        /* Http Request Part */
        assistRef.headers(request.headers());
        assistRef.uri(request.uri());
        assistRef.method(request.method());

        /* Session, User, Data */
        assistRef.session(context.session());
        assistRef.user(context.user());
        assistRef.context(context.data());
    }

    static void copyFrom(final Envelop envelopRef, final Envelop from) {
        if (Objects.nonNull(from)) {
            envelopRef.method(from.method());
            envelopRef.uri(from.uri());
            envelopRef.user(from.user());
            envelopRef.session(from.session());
            envelopRef.headers(from.headers());
            /*
             * Spec
             */
            envelopRef.acl(from.acl());
            envelopRef.key(from.key());
        }
    }

    static void copyTo(final Envelop envelopRef, final Envelop to) {
        to.method(envelopRef.method());
        to.uri(envelopRef.uri());
        to.user(envelopRef.user());
        to.session(envelopRef.session());
        to.headers(envelopRef.headers());
        /*
         * Spec
         */
        to.acl(envelopRef.acl());
        to.key(envelopRef.key());
    }
}
