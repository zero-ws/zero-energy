package io.zerows.core.security.atom.token;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/*
 * This token if for Basic authorization in Http client here
 * It could provide:
 * 1) token value
 * 2) authorization http header value based on Basic
 */
public class BasicToken implements WebToken {
    private final String token;
    private final String username;

    public BasicToken(final String username, final String password) {
        this.username = username;
        this.token = Ut.encryptBase64(username, password);
    }

    @Override
    public String token() {
        return this.token;
    }

    @Override
    public String authorization() {
        return "Basic " + this.token;
    }

    @Override
    public String user() {
        return this.username;
    }

    /**
     * <pre><code>
     *     {
     *         "username": "???",
     *         "token": "???"
     *     }
     * </code></pre>
     *
     * @return JsonObject
     */
    @Override
    public JsonObject data() {
        final JsonObject token = new JsonObject();
        token.put(KName.USERNAME, this.username);
        token.put(KName.TOKEN, this.token);
        return token;
    }
}
