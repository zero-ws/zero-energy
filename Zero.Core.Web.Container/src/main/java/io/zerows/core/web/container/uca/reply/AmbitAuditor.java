package io.zerows.core.web.container.uca.reply;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;
import io.zerows.core.domain.atom.element.JComponent;
import io.zerows.core.web.io.plugins.extension.PlugAuditor;
import io.zerows.core.web.model.commune.Envelop;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

import java.util.Objects;

/**
 * @author lang : 2024-06-27
 */
public class AmbitAuditor implements OAmbit {
    private static final Cc<String, PlugAuditor> CC_PLUGIN = Cc.openThread();
    private final PluginOption option;

    public AmbitAuditor() {
        final Bundle owner = FrameworkUtil.getBundle(this.getClass());
        this.option = PluginOption.of(owner);
    }

    @Override
    public Future<Envelop> then(final RoutingContext context, final Envelop envelop) {
        // 提取 auditor 部分的 JComponent
        final JComponent component = this.option.getComponent(YmlCore.extension.AUDITOR);
        if (Objects.isNull(component)) {
            return Future.succeededFuture(envelop);
        }


        final Class<?> auditorCls = component.getComponent();
        Objects.requireNonNull(auditorCls);
        final PlugAuditor auditor = CC_PLUGIN.pick(() -> Ut.instance(auditorCls), auditorCls.getName());
        if (Objects.isNull(auditor)) {
            return Future.succeededFuture(envelop);
        }


        return auditor.bind(component.getConfig()).audit(context, envelop);
    }
}
