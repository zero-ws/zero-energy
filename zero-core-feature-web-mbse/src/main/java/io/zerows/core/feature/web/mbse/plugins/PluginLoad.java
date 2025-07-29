package io.zerows.core.feature.web.mbse.plugins;

import io.horizon.uca.cache.Cc;
import io.modello.specification.action.HLoad;
import io.modello.specification.atom.HAtom;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PluginLoad {
    /*
     * Infusion for HLoad / HED
     */
    private static final Cc<String, HLoad> CC_PLUGIN_ATOM = Cc.openThread();

    public static HAtom atom(final String namespace, final String identifier) {
        return Ux.mountPlugin(YmlCore.extension.ATOM, (atomCls, config) -> {
            final HLoad loader = CC_PLUGIN_ATOM.pick(() -> Ut.instance(atomCls));
            /*
             * Bind configuration of
             * argument:
             *   component:
             *   config
             */
            return loader.atom(namespace, identifier);
        }, () -> null);
    }
}
