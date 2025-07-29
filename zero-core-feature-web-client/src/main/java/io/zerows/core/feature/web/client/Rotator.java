package io.zerows.core.feature.web.client;

import io.modello.atom.app.KIntegrationApi;
import io.vertx.core.json.JsonObject;
import org.apache.http.impl.client.CloseableHttpClient;

/*
 * Rotator for http request of major for methods:
 *
 * - POST
 * - GET
 * - PUT
 * - DELETE
 */
public interface Rotator {
    /*
     * The rotator could bind to HttpClient ( core )
     */
    Rotator bind(CloseableHttpClient client);

    /*
     * Request data with `Json` parameters, get string response
     */
    String request(KIntegrationApi request, JsonObject params);
}
