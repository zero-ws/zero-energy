package io.vertx.up.boot.anima;

import io.horizon.eon.em.web.EmTraffic;
import io.macrocosm.specification.config.HConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Service discovery usage.
 */
public class DetectScatter extends WorkerScatter {

    @Override
    protected Set<EmTraffic.Exchange> getModel(final HConfig config) {
        return new HashSet<EmTraffic.Exchange>() {
            {
                this.add(EmTraffic.Exchange.DISCOVERY_PUBLISH);
            }
        };
    }
}
