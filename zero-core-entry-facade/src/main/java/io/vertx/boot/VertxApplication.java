package io.vertx.boot;

import io.horizon.runtime.Runner;
import io.horizon.uca.boot.KLauncher;
import io.macrocosm.specification.config.HConfig;
import io.vertx.boot.supply.Electy;
import io.vertx.core.Vertx;
import io.vertx.up.eon.KMeta;
import io.zerows.core.web.container.uca.store.StubLinear;

/**
 * 标准启动器，直接启动 Vertx 实例处理 Zero 相关的业务逻辑
 */
public class VertxApplication {

    public static void run(final Class<?> clazz, final String... args) {
        // 构造启动器容器
        final KLauncher<Vertx> container = KLauncher.create(clazz, args);
        container.start(Electy.whenContainer(VertxApplication::runInternal));
    }

    public static void runInternal(final Vertx vertx, final HConfig config) {

        /* Agent 类型处理新流程 */
        Runner.run(() -> StubLinear.standalone(vertx, KMeta.Typed.AGENT), "component-agent");

        /* Worker 类型处理新流程 */
        Runner.run(() -> StubLinear.standalone(vertx, KMeta.Typed.WORKER), "component-worker");

        /* Infusion 插件处理新流程  **/
        Runner.run(() -> StubLinear.standalone(vertx, KMeta.Typed.INFUSION), "component-infix");

        /* Rule 验证规则处理流程 **/
        Runner.run(() -> StubLinear.standalone(vertx, KMeta.Typed.CODEX), "component-codex");
    }
}
