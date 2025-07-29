package io.zerows.core.metadata.atom.service.context;

import io.vertx.up.util.Ut;
import io.zerows.core.metadata.atom.configuration.MDConfiguration;
import io.zerows.core.metadata.eon.em.EmService;
import io.zerows.core.metadata.osgi.service.EnergyConfiguration;
import io.zerows.core.metadata.zdk.service.ServiceContext;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-07-01
 */
public class ContextOfModule extends ContextOfPlugin {

    private MDConfiguration configuration;

    public ContextOfModule(final Bundle owner) {
        super(owner);
    }

    @Override
    public MDConfiguration configuration() {
        return this.configuration;
    }

    @Override
    public ServiceContext putConfiguration(final MDConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    @Override
    public EmService.Context type() {
        return EmService.Context.MODULE;
    }

    protected EnergyConfiguration service() {
        return Ut.Bnd.service(EnergyConfiguration.class, this.owner());
    }
}
