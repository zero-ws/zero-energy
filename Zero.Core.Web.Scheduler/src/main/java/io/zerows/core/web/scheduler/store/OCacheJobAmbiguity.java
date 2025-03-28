package io.zerows.core.web.scheduler.store;

import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import io.zerows.core.web.scheduler.atom.Mission;
import org.osgi.framework.Bundle;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lang : 2024-04-20
 */
class OCacheJobAmbiguity extends AbstractAmbiguity implements OCacheJob {
    private final Set<Mission> jobs = new HashSet<>();

    OCacheJobAmbiguity(final Bundle bundle) {
        super(bundle);
    }

    @Override
    public Set<Mission> value() {
        return this.jobs;
    }

    @Override
    public OCacheJob add(final Set<Mission> missions) {
        this.jobs.addAll(missions);
        return this;
    }

    @Override
    public OCacheJob remove(final Set<Mission> missions) {
        this.jobs.removeAll(missions);
        return this;
    }
}
