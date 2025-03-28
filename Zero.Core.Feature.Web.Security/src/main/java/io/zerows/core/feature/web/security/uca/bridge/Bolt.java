package io.zerows.core.feature.web.security.uca.bridge;

import io.vertx.core.Vertx;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.util.Ut;
import io.zerows.core.security.atom.Aegis;
import io.zerows.core.security.zdk.Lee;
import io.zerows.core.security.zdk.LeeBuiltIn;
import io.zerows.core.security.zdk.LeeExtension;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Bolt {
    static Bolt get() {
        return BoltWhich.CC_BOLT.pick(BoltWhich::new);
    }

    static Lee reference(final EmSecure.AuthWall wall) {
        //final AuthWall wall = config.getType();
        if (EmSecure.AuthWall.EXTENSION == wall) {
            return BoltWhich.CC_LEE.pick(() -> Ut.service(LeeExtension.class), LeeExtension.class.getName());
        } else {
            return BoltWhich.CC_LEE.pick(() -> Ut.service(LeeBuiltIn.class), LeeBuiltIn.class.getName());
        }
    }

    /*
     * 1. Authenticate Handler
     */
    AuthenticationHandler authenticate(Vertx vertx, Aegis config);

    /*
     * 2. Authorization Handler
     */
    AuthorizationHandler authorization(Vertx vertx, Aegis config);

    AuthenticationProvider authenticateProvider(Vertx vertx, Aegis config);
}
