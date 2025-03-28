package io.zerows.core.metadata.osgi.dependency;

import io.macrocosm.specification.config.HSetting;
import io.zerows.core.metadata.atom.configuration.MDConfiguration;
import io.zerows.core.metadata.eon.em.EmService;
import io.zerows.core.metadata.osgi.service.EnergyConfiguration;
import io.zerows.core.metadata.zdk.dependency.OOnce;
import io.zerows.core.metadata.zdk.service.ServiceContext;

import java.util.Objects;

/**
 * @author lang : 2024-07-02
 */
public class OnceConfiguration implements OOnce.LifeCycle<EnergyConfiguration> {
    // 等待服务
    private volatile EnergyConfiguration cachedEnergyConfiguration;

    @Override
    public void bind(final Object reference) {
        if (reference instanceof final EnergyConfiguration energyConfiguration) {
            this.cachedEnergyConfiguration = energyConfiguration;
        }
    }

    @Override
    public boolean isReady() {
        return Objects.nonNull(this.cachedEnergyConfiguration);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> R start(final ServiceContext context) {
        // 追加配置相关信息
        final MDConfiguration configuration = context.configuration();
        if (Objects.nonNull(configuration)) {
            this.cachedEnergyConfiguration.addConfiguration(configuration);
        }

        // 追加入口相关信息
        if (EmService.Context.APP == context.type()) {
            final HSetting setting = context.setting();
            Objects.requireNonNull(setting);
            this.cachedEnergyConfiguration.addSetting(context.owner(), setting);
        }
        return (R) configuration;
    }

    @Override
    public void stop(final ServiceContext context) {

    }

    @Override
    public EnergyConfiguration reference() {
        return this.cachedEnergyConfiguration;
    }
}
