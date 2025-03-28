package io.zerows.core.web.scheduler.osgi.agent;

import io.horizon.eon.VValue;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Worker;
import io.vertx.up.eon.em.EmJob;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.environment.DevEnv;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.web.scheduler.atom.Mission;
import io.zerows.core.web.scheduler.uca.center.Agha;
import io.zerows.core.web.scheduler.uca.running.JobConfig;
import io.zerows.core.web.scheduler.uca.running.JobPin;
import io.zerows.core.web.scheduler.uca.running.JobStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Background worker of Zero framework, it's for schedule of background tasks here.
 * This scheduler is for task deployment, it should deploy all tasks
 * This worker must be SINGLE ( instances = 1 ) because multi worker with the same tasks may be
 * conflicts
 */
@Worker(instances = VValue.SINGLE)
public class ZeroScheduler extends AbstractVerticle {

    private static final OLog LOGGER = Ut.Log.vertx(ZeroScheduler.class);
    private static final JobStore STORE = JobPin.getStore();

    public ZeroScheduler() {
    }

    @Override
    public void start() {
        /* Whether contains JobConfig? */
        final JobConfig config = JobPin.getConfig();
        if (Objects.nonNull(config)) {
            /* Pick Up all Mission definition from system */
            final Set<Mission> missions = STORE.fetch();
            /* Whether there exist Mission definition */
            if (missions.isEmpty()) {
                LOGGER.info(INFO.JOB_EMPTY);
            } else {
                LOGGER.info(INFO.JOB_MONITOR, missions.size());
                /* Start each job here by different types */
                final List<Future<Void>> futures = new ArrayList<>();
                missions.forEach(mission -> futures.add(this.start(mission)));
                Fn.combineT(futures).onSuccess(nil -> LOGGER.info(INFO.JOB_STARTED));
            }
        } else {
            LOGGER.info(INFO.JOB_CONFIG_NULL);
        }
    }

    private Future<Void> start(final Mission mission) {
        /*
         * Prepare for mission, it's verf important to bind mission object to Vertx
         * instead of bind(Vertx) method.
         */
        final Object reference = mission.getProxy();
        if (Objects.nonNull(reference)) {
            /*
             * Bind vertx
             */
            Ut.contract(reference, Vertx.class, this.vertx);
        }
        /*
         * Agha calling
         */
        final Agha agha = Agha.get(mission.getType());
        if (Objects.nonNull(agha)) {
            /*
             * Bind vertx
             */
            Ut.contract(agha, Vertx.class, this.vertx);
            /*
             * Invoke here to provide input
             */
            if (DevEnv.devJobBoot()) {
                LOGGER.info(INFO.JOB_AGHA_SELECTED, agha.getClass(), mission.getCode(), mission.getType());
            }
            /*
             * If job type is ONCE, it's not started
             */
            if (EmJob.JobType.ONCE != mission.getType()) {
                agha.begin(mission);
            }
        }
        return Ut.future();
    }
}
