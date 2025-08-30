package io.zerows.core.web.mbse.uca.mixture;

import io.zerows.core.uca.cache.Cc;
import io.zerows.specification.modeling.operation.HLoad;
import io.zerows.specification.modeling.HAtom;
import io.zerows.core.web.mbse.plugins.PluginLoad;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HLoadSmart implements HLoad {
    private static final Cc<String, HLoad> CC_NORM = Cc.openThread();

    private final transient HLoad loader;

    public HLoadSmart() {
        this.loader = CC_NORM.pick(HLoadNorm::new, HLoadNorm.class.getName());
    }

    @Override
    public HAtom atom(final String appName, final String identifier) {
        /*
         * Load Sequence calculation
         *
         * 1. after = true
         *    HLoadNorm -> extension loader
         * 2. after = false
         *    extension loader -> HLoadNorm
         */
        // Default Situation
        // Static
        HAtom atom = this.loader.atom(appName, identifier);
        if (Objects.isNull(atom)) {
            // Dynamic
            atom = PluginLoad.atom(appName, identifier);
        }
        return atom;
    }
}
