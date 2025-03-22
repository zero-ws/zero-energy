package io.zerows.core.web.scheduler.uca.timer;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Contract;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.web.scheduler.atom.specification.KScheduler;
import io.zerows.core.web.scheduler.eon.MessageOfJob;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.function.Consumer;

public class VertxInterval implements Interval {
    private static final OLog LOGGER = Ut.Log.uca(VertxInterval.class);
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MM-dd HH:mm.ss.SSS");
    /*
     * Fix issue of delay < 1ms, the default should be 1
     * Cannot schedule a timer with delay < 1 ms
     */
    private static final int START_UP_MS = 1;

    @Contract
    private transient Vertx vertx;

    private Consumer<Long> controlFn;

    @Override
    public Interval bind(final Consumer<Long> controlFn) {
        this.controlFn = controlFn;
        return this;
    }

    /*
     * This situation is for executing without any `delay` part
     * here, although you have set the `delay` parameter in `Timer` here
     * but the code logical will ignore `delay`.
     *
     * For `delay` part the system should avoid following issue:
     * Fix issue of delay < 1ms, the default should be 1, Cannot schedule a timer with delay < 1 ms
     *
     * The 1ms is started. When following condition has been triggered, here are two code logical
     *
     * 1) KScheduler is null        ( Once Job )
     * 2) KScheduler is not null    ( Legacy Plan Job )
     */
    @Override
    public void startAt(final Handler<Long> actuator, final KScheduler timer) {
        if (Objects.isNull(timer)) {
            /*
             * Because timer is null, delay ms is not needed
             * In this kind of situation
             * call vertx.setTimer only, the smallest is 1ms ( Right Now )
             */
            LOGGER.info(MessageOfJob.INTERVAL.START);
            this.vertx.setTimer(START_UP_MS, actuator);
        } else {
            /*
             * Extract delay ms from `timer` reference
             * Be careful about the timerId here, the returned timerId
             *
             * Here are two timerId
             * 1. setTimer          ( Returned Directly )
             * 2. setPeriodic       ( Output by actuator )
             */
            final long waitSec = timer.waitUntil();
            final long delay = waitSec + START_UP_MS;
            this.vertx.setTimer(delay, ignored -> {
                final long duration = timer.waitDuration() + START_UP_MS;
                final long timerId = this.vertx.setPeriodic(duration, actuator);
                /*
                 * Bind the controlFn to consume the timerId of periodic timer
                 * In the document of vert.x here are comments:
                 *
                 * To cancel a periodic timer, call cancelTimer specifying the timer id. For example:
                 * vertx.cancelTimer(timerID);
                 */
                LOGGER.info(MessageOfJob.INTERVAL.SCHEDULED, String.valueOf(timerId), timer.name(), duration);
                if (Objects.nonNull(this.controlFn)) {
                    this.controlFn.accept(timerId);
                }
            });
            LOGGER.info(MessageOfJob.INTERVAL.DELAY_START, timer.name(), FORMATTER.format(Ut.toDuration(waitSec)));
        }
    }

    @Override
    public void restartAt(final Handler<Long> actuator, final KScheduler timer) {
        if (Objects.isNull(timer)) {
            LOGGER.info(MessageOfJob.INTERVAL.RESTART);
            this.vertx.setTimer(START_UP_MS, actuator);
        } else {
            final long waitSec = timer.waitUntil();
            final long delay = waitSec + START_UP_MS;
            this.vertx.setTimer(delay, actuator);
            LOGGER.info(MessageOfJob.INTERVAL.DELAY_RESTART, timer.name(), FORMATTER.format(Ut.toDuration(waitSec)));
        }
    }
}
