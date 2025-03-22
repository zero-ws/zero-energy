package io.vertx.up.unity;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.web.scheduler.plugins.JobClient;
import io.zerows.core.web.scheduler.plugins.JobInfix;

public class UxJob {
    private static final OLog LOGGER = Ut.Log.job(UxJob.class);
    private transient final JobClient client;

    UxJob() {
        this.client = JobInfix.getClient();
    }

    // Start job
    public Future<Boolean> startAsync(final String code) {
        return Fn.pack(future -> this.client.startAsync(code, res -> {
            LOGGER.info(INFO.UxJob.JOB_START, code, res.result());
            future.complete(Boolean.TRUE);
        }));
    }

    // Stop job
    public Future<Boolean> stopAsync(final String code) {
        return Fn.pack(future -> this.client.stopAsync(code,
            res -> {
                LOGGER.info(INFO.UxJob.JOB_STOP, code);
                future.complete(Boolean.TRUE);
            }));
    }

    // Resume job
    public Future<Boolean> resumeAsync(final String code) {
        return Fn.pack(future -> this.client.resumeAsync(code,
            res -> {
                LOGGER.info(INFO.UxJob.JOB_RESUME, code);
                future.complete(Boolean.TRUE);
            }));
    }

    public Future<JsonObject> statusAsync(final String namespace) {
        return this.client.statusAsync(namespace);
    }
}
