package io.zerows.core.web.scheduler.uca.running;

import io.vertx.up.fn.Fn;
import io.zerows.core.web.scheduler.atom.Mission;
import io.zerows.core.web.scheduler.store.OCacheJob;

import java.util.Set;

/**
 * Bridge for different JobStore
 */
class CodeStore implements JobReader {

    @Override
    public Set<Mission> fetch() {
        return OCacheJob.entireValue();
    }

    @Override
    public Mission fetch(final String code) {
        return Fn.runOr(null, () -> this.fetch().stream()
            .filter(mission -> code.equals(mission.getCode()))
            .findFirst().orElse(null), code);
    }
}
