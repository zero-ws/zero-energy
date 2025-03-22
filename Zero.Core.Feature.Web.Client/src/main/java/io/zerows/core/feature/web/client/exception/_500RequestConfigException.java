package io.zerows.core.feature.web.client.exception;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.exception.WebException;
import io.modello.atom.app.KIntegrationApi;
import io.vertx.core.json.JsonObject;

public class _500RequestConfigException extends WebException {

    public _500RequestConfigException(final Class<?> clazz,
                                      final KIntegrationApi request,
                                      final JsonObject data) {
        super(clazz, request.toString(), data.encode());
    }

    @Override
    public int getCode() {
        return -60046;
    }

    @Override
    public HttpStatusCode getStatus() {
        return HttpStatusCode.INTERNAL_SERVER_ERROR;
    }
}
