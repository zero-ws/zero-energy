package io.zerows.core.feature.web.websocket.store;

import io.zerows.core.feature.web.websocket.atom.Remind;
import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import org.osgi.framework.Bundle;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lang : 2024-04-21
 */
class OCacheSockAmbiguity extends AbstractAmbiguity implements OCacheSock {
    private final Set<Remind> socks = new HashSet<>();

    OCacheSockAmbiguity(final Bundle bundle) {
        super(bundle);
    }

    @Override
    public Set<Remind> value() {
        return this.socks;
    }

    @Override
    public OCacheSock add(final Set<Remind> socks) {
        this.socks.addAll(socks);
        return this;
    }

    @Override
    public OCacheSock remove(final Set<Remind> socks) {
        this.socks.removeAll(socks);
        return this;
    }
}
