package io.zerows.core.assembly.store;

import io.horizon.runtime.Runner;
import io.macrocosm.specification.config.HSetting;
import io.vertx.up.eon.KMeta;
import io.vertx.up.util.Ut;
import io.zerows.core.assembly.uca.scan.*;
import io.zerows.core.metadata.store.OCacheClass;
import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import io.zerows.core.metadata.zdk.running.ORepository;
import io.zerows.core.metadata.zdk.uca.Inquirer;
import org.osgi.framework.Bundle;

import java.util.Set;

/**
 * 中间层，针对扫描出现的二次处理过程，不同环境双用模式
 * <pre><code>
 *     1. 全局环境
 *     2. OSGI 环境，会调用此处的服务来执行添加，OSGI 不走底层服务处理
 * </code></pre>
 *
 * @author lang : 2024-04-19
 */
public class ORepositoryClass extends AbstractAmbiguity implements ORepository {

    public ORepositoryClass(final Bundle bundle) {
        super(bundle);
    }

    /**
     * 全局环境专用方法
     * <pre><code>
     *     1. {@link io.vertx.up.annotations.Infusion} - {@link InquirerPlugin}
     *     2. {@link io.vertx.up.annotations.Queue} - {@link InquirerQueue}
     *     3. {@link io.vertx.up.annotations.EndPoint} - {@link InquirerEndPoint}
     *     4. {@link io.vertx.up.annotations.Worker} - {@link InquirerMWorker}
     *     5. {@link io.vertx.up.annotations.Agent} - {@link InquirerMAgent}
     * </code></pre>
     */
    @Override
    public void whenStart(final HSetting setting) {

        this.whenInternal(setting);
    }

    @Override
    public void whenUpdate(final HSetting setting) {

        this.whenInternal(setting);
    }

    private void whenInternal(final HSetting setting) {
        // 读取全局类缓存
        final OCacheClass processor = OCacheClass.of(this.caller());
        final long start = System.currentTimeMillis();
        Runner.run("meditate-class",
            // @Infusion
            () -> {
                final Inquirer<Set<Class<?>>> plugins = Ut.singleton(InquirerPlugin.class);
                processor.compile(KMeta.Typed.INFUSION, plugins::scan);
            },
            // @Queue
            () -> {
                final Inquirer<Set<Class<?>>> queues = Ut.singleton(InquirerQueue.class);
                processor.compile(KMeta.Typed.QUEUE, queues::scan);
            },
            // @EndPoint
            () -> {
                final Inquirer<Set<Class<?>>> endPoints = Ut.singleton(InquirerEndPoint.class);
                processor.compile(KMeta.Typed.ENDPOINT, endPoints::scan);
            },
            // Dot / @Worker
            () -> {
                final Inquirer<Set<Class<?>>> workers = Ut.singleton(InquirerMWorker.class);
                processor.compile(KMeta.Typed.WORKER, workers::scan);
            },
            // Dot / @Agent
            () -> {
                final Inquirer<Set<Class<?>>> agents = Ut.singleton(InquirerMAgent.class);
                processor.compile(KMeta.Typed.AGENT, agents::scan);
            },
            // Dot / @Agent ( type = ServerType )
            () -> {
                final Inquirer<Set<Class<?>>> rpcs = Ut.singleton(InquirerIpc.class);
                processor.compile(KMeta.Typed.IPC, rpcs::scan);
            }
        );
        final long end = System.currentTimeMillis();
        Ut.Log.boot(ORepositoryClass.class).info(" {0}ms / Zero Timer: Meditate Class Scanned! key = {1}",
            end - start, Ut.Bnd.keyCache(this.caller(), ORepositoryClass.class));
    }

    @Override
    public void whenRemove() {
        // 缓存类
        final OCacheClass processor = OCacheClass.of(this.caller());
        // 提取移除信息
        final Set<Class<?>> uninstallData = processor.value();
        // 移除
        processor.remove(uninstallData);
    }
}
