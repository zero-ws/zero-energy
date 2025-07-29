package io.vertx.up.unity;

import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.WorkerExecutor;
import io.vertx.core.eventbus.EventBus;
import io.vertx.up.util.Ut;
import io.zerows.core.configuration.uca.transformer.VertxTransformer;
import io.zerows.core.web.model.commune.Envelop;
import io.zerows.core.web.model.uca.codec.EnvelopCodec;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

class VertxNative {

    private static Vertx VERTX_NATIVE;

    private static synchronized Vertx nativeRef() {
        synchronized (VertxNative.class) {
            if (Objects.isNull(VERTX_NATIVE)) {
                VERTX_NATIVE = Vertx.vertx(VertxTransformer.nativeOption());
                final EventBus eventBus = VERTX_NATIVE.eventBus();
                eventBus.registerDefaultCodec(Envelop.class, Ut.singleton(EnvelopCodec.class));
            }
            return VERTX_NATIVE;
        }
    }

    static Vertx nativeVertx() {
        return nativeRef();
    }

    static WorkerExecutor nativeWorker(final String name, final Vertx vertx, final Integer minutes) {
        return vertx.createSharedWorkerExecutor(name, 2, minutes, TimeUnit.MINUTES);
    }

    static <T> io.vertx.core.Future<T> nativeWorker(final String name, final Vertx vertx, final Handler<Promise<T>> handler) {
        final Promise<T> promise = Promise.promise();
        final WorkerExecutor executor = nativeWorker(name, vertx, 10);
        executor.executeBlocking(
            handler,
            post -> {
                // Fix Issue:
                // Task io.vertx.core.impl.TaskQueue$$Lambda$367/0x00000008004f3440@2b1e3784 rejected from
                // java.util.concurrent.ThreadPoolExecutor@1db1d316
                // [Terminated, pool size = 0, active threads = 0, queued tasks = 0, completed tasks = 0]
                executor.close();
                promise.complete(post.result());
            }
        );
        return promise.future();
    }
}
