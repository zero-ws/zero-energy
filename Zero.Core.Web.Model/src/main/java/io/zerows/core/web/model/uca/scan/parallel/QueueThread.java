package io.zerows.core.web.model.uca.scan.parallel;

import io.horizon.uca.log.Annal;
import io.vertx.up.util.Ut;
import io.zerows.core.web.model.atom.Receipt;
import io.zerows.core.web.model.uca.extract.Extractor;
import io.zerows.core.web.model.uca.extract.ExtractorReceipt;

import java.util.HashSet;
import java.util.Set;

public class QueueThread extends Thread {

    private static final Annal LOGGER = Annal.get(QueueThread.class);

    private final Set<Receipt> receipts = new HashSet<>();

    private final transient Extractor<Set<Receipt>> extractor =
        Ut.instance(ExtractorReceipt.class);

    private final transient Class<?> reference;

    public QueueThread(final Class<?> clazz) {
        this.setName("zero-queue-scanner-" + this.getId());
        this.reference = clazz;
    }

    @Override
    public void run() {
        if (null != this.reference) {
            this.receipts.addAll(this.extractor.extract(this.reference));
            LOGGER.info(INFO.SCANNED_RECEIPTS, this.reference.getName(),
                this.receipts.size());
        }
    }

    public Set<Receipt> getReceipts() {
        return this.receipts;
    }
}
