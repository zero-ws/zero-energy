package io.zerows.core.feature.web.websocket.store;

import io.macrocosm.specification.config.HSetting;
import io.vertx.up.util.Ut;
import io.zerows.core.feature.web.websocket.atom.Remind;
import io.zerows.core.metadata.store.OCacheClass;
import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import io.zerows.core.metadata.zdk.running.ORepository;
import io.zerows.core.metadata.zdk.uca.Inquirer;
import org.osgi.framework.Bundle;

import java.util.Set;

/**
 * @author lang : 2024-04-21
 */
public class ORepositorySock extends AbstractAmbiguity implements ORepository {


    protected ORepositorySock(final Bundle bundle) {
        super(bundle);
    }

    @Override
    public void whenStart(final HSetting setting) {

        final OCacheSock processor = OCacheSock.of(this.caller());
        final Inquirer<Set<Remind>> scanner = Ut.singleton(SockInquirer.class);
        processor.add(scanner.scan(OCacheClass.entireValue()));
    }
}
