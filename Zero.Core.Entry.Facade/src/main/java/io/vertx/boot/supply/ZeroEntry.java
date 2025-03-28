package io.vertx.boot.supply;

import io.macrocosm.specification.config.HConfig;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.fn.Fn;
import io.zerows.core.cloud.util.Ho;
import io.zerows.core.metadata.store.OZeroStore;
import io.zerows.core.web.container.store.BootStore;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @author lang : 2023-06-10
 */
class ZeroEntry {
    private static final String MSG_EXT_CONFIGURATION = "Extension configuration missing {0}";

    static BiConsumer<Vertx, HConfig> whenInstruction(final BiConsumer<Vertx, HConfig> endFn) {
        // 1. 环境注册
        return (vertx, config) -> ZeroEnroll.registryStart(vertx, config)
            /*
             * Fix: Cannot invoke "io.vertx.mod.ambient.atom.AtConfig.getInitializer()"
             * because the return value of "io.vertx.mod.ambient.init.AtPin.getConfig()" is null
             */
            // 2. 是否执行扩展，调用 Fn.passion 带顺序
            .compose(arkSet -> whenExtension(config, () -> Fn.passion(Boolean.TRUE,
                // 2.1. 扩展：配置
                done -> ZeroEnroll.registryAmbient(vertx, config, arkSet)
            )))


            // 3. 注册结束之后的统一回调
            .onComplete(ZeroEnroll.registryEnd(() -> endFn.accept(vertx, config)));
    }

    static BiConsumer<Vertx, HConfig> whenContainer(final BiConsumer<Vertx, HConfig> endFn) {
        // 1. 环境注册
        return (vertx, config) -> ZeroEnroll.registryStart(vertx, config)


            // 2. 是否执行扩展，调用 Fn.passion 带顺序
            .compose(arkSet -> whenExtension(config, () -> Fn.passion(Boolean.TRUE,
                // 2.1. 扩展：配置
                done -> ZeroEnroll.registryAmbient(vertx, config, arkSet),
                // 2.2. 扩展：初始化
                done -> ZeroEnroll.registryArk(vertx, config, arkSet))
            ))


            // 3. 注册结束之后的统一回调
            .onComplete(ZeroEnroll.registryEnd(() -> endFn.accept(vertx, config)));
    }

    private static Future<Boolean> whenExtension(final HConfig config, final Supplier<Future<Boolean>> supplier) {
        /*
         * 新版不再支持旧模式注册：
         * init:
         *    extension:
         *      - component: xxx
         *    compile:
         *      - component: xxx
         *        order: 1
         * 全流程执行
         * 1. (PlugIn) 配置之前
         *    执行模块配置
         * 2. (PlugIn）初始化之前
         *    执行模块初始化
         */
        final BootStore store = BootStore.singleton();
        if (!store.isInit()) {
            Ho.LOG.Env.info(ZeroEnroll.class, MSG_EXT_CONFIGURATION, config);
            return Future.succeededFuture(Boolean.TRUE);
        }
        if (!OZeroStore.is(YmlCore.init.__KEY)) {
            return Future.succeededFuture(Boolean.TRUE);
        }
        return supplier.get();
    }
}
