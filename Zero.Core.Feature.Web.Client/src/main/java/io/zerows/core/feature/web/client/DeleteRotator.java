package io.zerows.core.feature.web.client;

import io.modello.atom.app.KIntegration;
import io.modello.atom.app.KIntegrationApi;
import io.vertx.core.json.JsonObject;
import org.apache.http.client.methods.HttpDelete;

public class DeleteRotator extends AbstractRotator {

    DeleteRotator(final KIntegration integration) {
        super(integration);
    }

    @Override
    public String request(final KIntegrationApi request, final JsonObject params) {
        final HttpDelete httpDelete = new HttpDelete(this.configPath(request, params));
        this.logger().info(INFO.HTTP_REQUEST, request.getPath(), request.getMethod(), params);
        return this.sendUrl(httpDelete, request.getHeaders());
    }
}
