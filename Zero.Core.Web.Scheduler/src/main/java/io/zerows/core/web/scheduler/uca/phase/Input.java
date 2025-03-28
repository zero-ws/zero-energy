package io.zerows.core.web.scheduler.uca.phase;

import io.horizon.atom.program.KRef;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.web.model.commune.Envelop;
import io.zerows.core.web.scheduler.atom.Mission;
import io.zerows.core.web.scheduler.eon.MessageOfJob;
import io.zerows.core.web.scheduler.zdk.JobIncome;

import java.util.Objects;

class Input {

    private static final OLog LOGGER = Ut.Log.uca(Input.class);

    private transient final Vertx vertx;
    private transient final KRef underway = new KRef();

    Input(final Vertx vertx) {
        this.vertx = vertx;
    }

    KRef underway() {
        return this.underway;
    }

    Future<Envelop> inputAsync(final Mission mission) {
        /*
         * Get income address
         * 1) If there configured income address, it means that there are some inputs came from
         *     'incomeAddress' ( For feature usage )
         * 2) No incomeAddress configured is often used for the job.
         * */
        final String address = mission.getIncomeAddress();
        if (Ut.isNil(address)) {
            /*
             * Event bus did not provide any input here
             */
            Element.onceLog(mission, () -> LOGGER.info(MessageOfJob.PHASE.PHASE_1ST_JOB, mission.getCode()));

            return Future.succeededFuture(Envelop.okJson());
        } else {
            /*
             * Event bus provide input and then it will pass to @On
             */
            LOGGER.info(MessageOfJob.PHASE.UCA_EVENT_BUS, "KIncome", address);
            final Promise<Envelop> input = Promise.promise();
            final EventBus eventBus = this.vertx.eventBus();
            eventBus.<Envelop>consumer(address, handler -> {

                Element.onceLog(mission, () -> LOGGER.info(MessageOfJob.PHASE.PHASE_1ST_JOB_ASYNC, mission.getCode(), address));

                final Envelop envelop = handler.body();
                if (Objects.isNull(envelop)) {
                    /*
                     * Success
                     */
                    input.complete(Envelop.ok());
                } else {
                    /*
                     * Failure
                     */
                    input.complete(envelop);
                }
            }).completionHandler(item -> {
                /*
                 * This handler will cause finally for future
                 * If no data came from address
                 */
                final Object result = item.result();
                if (Objects.isNull(result)) {
                    input.complete(Envelop.ok());
                } else {
                    input.complete(Envelop.success(result));
                }
            });
            return input.future();
        }
    }

    Future<Envelop> incomeAsync(final Envelop envelop, final Mission mission) {
        if (envelop.valid()) {
            /*
             * Get JobIncome
             */
            final JobIncome income = Element.income(mission);
            if (Objects.isNull(income)) {
                /*
                 * Directly
                 */
                Element.onceLog(mission, () -> LOGGER.info(MessageOfJob.PHASE.PHASE_2ND_JOB, mission.getCode()));
                return Future.succeededFuture(envelop);
            } else {
                /*
                 * JobIncome processing here
                 * Contract for vertx/mission
                 */
                LOGGER.info(MessageOfJob.PHASE.UCA_COMPONENT, "JobIncome", income.getClass().getName());
                /*
                 * JobIncome must define
                 * - Vertx reference
                 * - Mission reference
                 */
                Ut.contract(income, Vertx.class, this.vertx);
                Ut.contract(income, Mission.class, mission);
                /*
                 * Here we could calculate directory
                 */
                Element.onceLog(mission, () -> LOGGER.info(MessageOfJob.PHASE.PHASE_2ND_JOB_ASYNC, mission.getCode(), income.getClass().getName()));

                return income.underway().compose(refer -> {
                    /*
                     * Here provide extension for JobIncome
                     * 1 - You can do some operations in JobIncome to calculate underway data such as
                     *     dictionary data here.
                     * 2 - Also you can put some assist data into `KRef`, this `KRef` will be used
                     *     by major code logical instead of `re-calculate` the data again.
                     * 3 - For performance design, this structure could be chain passed in:
                     *     KIncome -> Job ( Channel ) -> Outcome
                     *
                     * Critical:  It's only supported by `Actor/Job` structure instead of `Api` passive
                     *     mode in Http Request / Response. it means that Api could not support this feature.
                     */
                    this.underway.add(refer.get());
                    return income.beforeAsync(envelop);
                });
            }
        } else {
            Element.onceLog(mission, () -> LOGGER.info(MessageOfJob.PHASE.ERROR_TERMINAL, mission.getCode(), envelop.error().getClass().getName()));
            return Ut.future(envelop);
        }
    }
}
