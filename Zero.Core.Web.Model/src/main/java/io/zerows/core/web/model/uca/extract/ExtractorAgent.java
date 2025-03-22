package io.zerows.core.web.model.uca.extract;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.DeploymentOptions;
import io.vertx.up.fn.Fn;
import io.zerows.core.configuration.atom.NodeNetwork;
import io.zerows.core.configuration.atom.NodeVertx;
import io.zerows.core.configuration.store.OCacheNode;

/**
 * Standard bottle deployment.
 */
public class ExtractorAgent implements Extractor<DeploymentOptions> {

    private static final Annal LOGGER = Annal.get(ExtractorAgent.class);
    private static final Cc<Class<?>, DeploymentOptions> CC_OPTIONS = Cc.open();

    @Override
    public DeploymentOptions extract(final Class<?> clazz) {
        Fn.runAt(() -> LOGGER.info(INFO.AGENT_HIT, clazz.getName()), clazz);

        final NodeNetwork network = OCacheNode.of().network();
        final NodeVertx nodeVertx = network.get();

        return CC_OPTIONS.pick(() -> nodeVertx.optionDeployment(clazz), clazz);
        // Fn.po?l(OPTIONS, clazz, () -> rotate.spinAgent(clazz));
    }
}
