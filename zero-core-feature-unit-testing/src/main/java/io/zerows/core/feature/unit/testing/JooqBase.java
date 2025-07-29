package io.zerows.core.feature.unit.testing;

import io.zerows.core.assembly.uca.di.DiPlugin;
import io.zerows.core.feature.database.jooq.JooqInfix;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class JooqBase extends ZeroBase {
    static {
        JooqInfix.init(VERTX);
    }

    protected <T> T component(final Class<?> clazz) {
        final DiPlugin plugin = DiPlugin.create(this.getClass());
        return plugin.createSingleton(clazz);
    }
}
