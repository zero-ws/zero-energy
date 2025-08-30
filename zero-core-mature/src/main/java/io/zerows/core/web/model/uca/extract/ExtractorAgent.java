package io.zerows.core.web.model.uca.extract;

import io.vertx.core.DeploymentOptions;
import io.zerows.core.fn.Fn;
import io.zerows.core.uca.cache.Cc;
import io.zerows.core.uca.log.Annal;
import io.zerows.module.configuration.atom.NodeNetwork;
import io.zerows.module.configuration.atom.NodeVertx;
import io.zerows.module.configuration.store.OCacheNode;

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
