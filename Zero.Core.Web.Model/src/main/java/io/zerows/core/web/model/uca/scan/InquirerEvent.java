package io.zerows.core.web.model.uca.scan;

import io.horizon.uca.log.Annal;
import io.vertx.up.fn.Fn;
import io.zerows.core.metadata.zdk.uca.Inquirer;
import io.zerows.core.web.model.atom.Event;
import io.zerows.core.web.model.uca.scan.parallel.EndPointThread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class InquirerEvent implements Inquirer<Set<Event>> {

    private static final Annal LOGGER = Annal.get(InquirerEvent.class);

    @Override
    public Set<Event> scan(final Set<Class<?>> endpoints) {
        final List<EndPointThread> threadReference = new ArrayList<>();
        /* 2.1.Build Api metadata **/
        for (final Class<?> endpoint : endpoints) {
            final EndPointThread thread =
                new EndPointThread(endpoint);
            threadReference.add(thread);
            thread.start();
        }
        /* 3.2. Join **/
        Fn.jvmAt(() -> {
            for (final EndPointThread item : threadReference) {
                item.join();
            }
        }, LOGGER);
        /* 3.3. Finally **/
        final Set<Event> events = new HashSet<>();
        Fn.jvmAt(() -> {
            for (final EndPointThread item : threadReference) {
                events.addAll(item.getEvents());
            }
        }, LOGGER);
        return events;
    }
}
