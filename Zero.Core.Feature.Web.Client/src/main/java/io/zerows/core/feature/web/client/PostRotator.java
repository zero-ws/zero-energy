package io.zerows.core.feature.web.client;

import io.modello.atom.app.KIntegration;
import io.modello.atom.app.KIntegrationApi;
import io.vertx.core.json.JsonObject;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

public class PostRotator extends AbstractRotator {

    PostRotator(final KIntegration integration) {
        super(integration);
    }

    @Override
    public String request(final KIntegrationApi request, final JsonObject params) {
        /*
         * HttpPost
         * */
        final HttpPost httpPost = new HttpPost(this.configPath(request, params));
        final StringEntity body = this.dataJson(params);
        this.logger().info(INFO.HTTP_REQUEST, request.getPath(), request.getMethod(), params);
        return this.sendEntity(httpPost, body, request.getHeaders());
    }
}
