package io.zerows.core.web.mbse.plugins;

import io.zerows.core.uca.cache.Cc;
import io.zerows.specification.modeling.operation.HLoad;
import io.zerows.specification.modeling.HAtom;
import io.zerows.core.constant.configure.YmlCore;
import io.zerows.unity.Ux;
import io.zerows.core.util.Ut;

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
