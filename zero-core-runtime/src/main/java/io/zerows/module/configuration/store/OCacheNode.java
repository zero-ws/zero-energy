package io.zerows.module.configuration.store;

import io.zerows.core.uca.cache.Cc;
import io.zerows.core.util.Ut;
import io.zerows.module.configuration.atom.NodeNetwork;
import io.zerows.module.configuration.atom.NodeVertx;
import io.zerows.module.metadata.zdk.running.OCache;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-04-20
 */
public interface OCacheNode extends OCache<NodeVertx> {

    Cc<String, OCacheNode> CC_SKELETON = Cc.open();

    static OCacheNode of(final Bundle bundle) {
        return CC_SKELETON.pick(() -> new OCacheNodeAmbiguity(bundle),
            Ut.Bnd.keyCache(bundle, OCacheNodeAmbiguity.class));
    }

    static OCacheNode of() {
        return of(null);
    }

    NodeNetwork network();
}
