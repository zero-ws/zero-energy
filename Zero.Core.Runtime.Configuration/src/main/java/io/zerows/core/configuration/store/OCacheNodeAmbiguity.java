package io.zerows.core.configuration.store;

import io.zerows.core.configuration.atom.NodeNetwork;
import io.zerows.core.configuration.atom.NodeVertx;
import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-04-20
 */
class OCacheNodeAmbiguity extends AbstractAmbiguity implements OCacheNode {

    private static final NodeNetwork NETWORK = new NodeNetwork();

    OCacheNodeAmbiguity(final Bundle bundle) {
        super(bundle);
    }

    // -------------- 比较特殊，全部为全局方法，唯一的网络节点 -------------------
    @Override
    public NodeVertx valueGet(final String name) {
        return NETWORK.get(name);
    }

    @Override
    public NodeNetwork network() {
        return NETWORK;
    }

    @Override
    public OCacheNode add(final NodeVertx nodeVertx) {
        NETWORK.add(nodeVertx.name(), nodeVertx);
        return this;
    }

    @Override
    public OCacheNode remove(final NodeVertx nodeVertx) {
        NETWORK.remove(nodeVertx.name());
        return this;
    }
}
