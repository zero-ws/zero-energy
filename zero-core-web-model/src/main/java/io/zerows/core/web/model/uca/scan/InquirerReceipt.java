package io.zerows.core.web.model.uca.scan;

import io.horizon.uca.log.Annal;
import io.vertx.up.fn.Fn;
import io.zerows.core.metadata.zdk.uca.Inquirer;
import io.zerows.core.web.model.atom.Receipt;
import io.zerows.core.web.model.uca.scan.parallel.QueueThread;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Receipt annotation scan
 * This thread is for Receipt extraction
 */
public class InquirerReceipt implements Inquirer<Set<Receipt>> {

    private static final Annal LOGGER = Annal.get(InquirerReceipt.class);

    @Override
    public Set<Receipt> scan(final Set<Class<?>> queues) {
        final List<QueueThread> threadReference = new ArrayList<>();
        /* 3.1. Build Metadata **/
        for (final Class<?> queue : queues) {
            final QueueThread thread =
                new QueueThread(queue);
            threadReference.add(thread);
            thread.start();
        }
        /* 3.2. Join **/
        Fn.jvmAt(() -> {
            for (final QueueThread item : threadReference) {
                item.join();
            }
        }, LOGGER);
        /* 3.3. Return **/
        final Set<Receipt> receipts = new HashSet<>();
        Fn.jvmAt(() -> threadReference.stream()
            .map(QueueThread::getReceipts)
            .forEach(receipts::addAll), LOGGER);
        /* 3.4. New Receipts replaced with Aeon System ( Enabled ) */
        return receipts;
    }
}
