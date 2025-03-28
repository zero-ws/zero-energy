package io.vertx.boot;

import io.horizon.runtime.Runner;
import io.horizon.uca.boot.KLauncher;
import io.macrocosm.specification.config.HConfig;
import io.vertx.boot.supply.Electy;
import io.vertx.core.Vertx;
import io.vertx.up.eon.KMeta;
import io.zerows.core.web.container.uca.store.StubLinear;

/**
 * Vertx EmApp begin launcher for api gateway.
 * It's only used in Micro Service mode.
 */
public class MicroApplication {

    public static void run(final Class<?> clazz, final String... args) {
        // 构造启动器容器
        final KLauncher<Vertx> container = KLauncher.create(clazz, args);
        container.start(Electy.whenContainer(MicroApplication::runComponent));
    }

    private static void runComponent(final Vertx vertx, final HConfig config) {
        /* 1.Find Agent for deploy **/
        Runner.run(() -> StubLinear.standalone(vertx, KMeta.Typed.IPC), "component-gateway");
        /* 2.Find Worker for deploy **/
        Runner.run(() -> StubLinear.standalone(vertx, KMeta.Typed.WORKER), "component-worker");
        /* 3.Initialize Infusion **/
        Runner.run(() -> StubLinear.standalone(vertx, KMeta.Typed.INFUSION), "component-infix");
    }
}
