package io.zerows.core.feature.web.security.osgi.dependency;

import io.vertx.up.util.Ut;
import io.zerows.core.feature.web.security.osgi.service.EnergySecure;
import io.zerows.core.metadata.zdk.dependency.OOnce;
import io.zerows.core.metadata.zdk.service.ServiceContext;

/**
 * @author lang : 2024-04-28
 */
public class OnceSecurity implements OOnce.LifeCycle<EnergySecure> {
    private /* 安全管理器 */ EnergySecure cachedSecure;

    @Override
    public void bind(final Object reference) {
        if (reference instanceof final EnergySecure secure) {
            this.cachedSecure = secure;
        }
    }

    @Override
    public boolean isReady() {
        return Ut.isNotNull(
            this.cachedSecure
        );
    }

    @Override
    public <R> R start(final ServiceContext context) {
        this.cachedSecure.install(context.owner());
        return null;
    }

    @Override
    public void stop(final ServiceContext context) {
        this.cachedSecure.uninstall(context.owner());
    }

    @Override
    public EnergySecure reference() {
        return this.cachedSecure;
    }
}
