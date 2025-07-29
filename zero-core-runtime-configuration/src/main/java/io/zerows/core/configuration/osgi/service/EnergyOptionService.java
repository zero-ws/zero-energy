package io.zerows.core.configuration.osgi.service;

import io.macrocosm.specification.config.HSetting;
import io.zerows.core.configuration.atom.NodeNetwork;
import io.zerows.core.configuration.atom.NodeVertx;
import io.zerows.core.configuration.store.OCacheNode;
import io.zerows.core.configuration.store.ORepositoryOption;
import io.zerows.core.metadata.zdk.running.ORepository;
import io.zerows.core.metadata.zdk.service.ServiceContext;
import org.osgi.framework.Bundle;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lang : 2024-04-28
 */
public class EnergyOptionService implements EnergyOption {
    @Override
    public NodeNetwork network(final ServiceContext context) {
        final Bundle caller = context.owner();
        final HSetting setting = context.setting();

        ORepository.ofOr(ORepositoryOption.class, caller).whenUpdate(setting);

        return OCacheNode.of(caller).network();
    }

    @Override
    public Set<NodeVertx> vertx(final ServiceContext context) {
        final NodeNetwork network = this.network(context);
        return new HashSet<>(network.vertxOptions().values());
    }
}
