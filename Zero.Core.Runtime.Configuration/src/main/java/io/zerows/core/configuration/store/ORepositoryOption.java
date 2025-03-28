package io.zerows.core.configuration.store;

import io.macrocosm.specification.config.HSetting;
import io.vertx.up.util.Ut;
import io.zerows.core.configuration.atom.NodeNetwork;
import io.zerows.core.configuration.uca.setup.CommonProcessor;
import io.zerows.core.configuration.zdk.Processor;
import io.zerows.core.metadata.store.OZeroStore;
import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import io.zerows.core.metadata.zdk.running.ORepository;
import org.osgi.framework.Bundle;

import java.util.Objects;

/**
 * @author lang : 2024-04-20
 */
public class ORepositoryOption extends AbstractAmbiguity implements ORepository {

    public ORepositoryOption(final Bundle bundle) {
        super(bundle);
    }

    /**
     * 单机版和非单机版统一调用此流程实现配置的基本管理，直接根据传入的 {@link HSetting} 来执行配置的初始化流程
     * <pre><code>
     *     1. 单机版：{@link OZeroStore#setting()}
     *     2. OSGI版：执行其他流程
     * </code></pre>
     *
     * <pre><code>
     *     1. {@link Bundle}
     *     2. {@link HSetting}
     * </code></pre>
     */
    @Override
    public void whenStart(final HSetting setting) {
        // 构造缓存基础数据
        final NodeNetwork network = OCacheNode.of(this.caller()).network();

        // 构造处理器
        final Processor<NodeNetwork, HSetting> processor = CommonProcessor.of();
        processor.makeup(network, setting);
    }

    @Override
    public void whenUpdate(final HSetting setting) {
        Objects.requireNonNull(this.caller());
        // 构造缓存基础数据
        final NodeNetwork network = OCacheNode.of(this.caller()).network();

        /*
         * 和单机版不一样，单机版本会在类就在时就初始化 NodeNetwork，而多个 Bundle 安装时有可能每个 NodeNetwork 的安装流程不一致，
         * 所以此处会判断是否已经初始化，如果已经初始化则不再初始化，否则会初始化。
         */
        if (!network.isReady()) {
            final Processor<NodeNetwork, HSetting> processor = CommonProcessor.of();
            processor.makeup(network, setting);
            Ut.Log.energy(this.getClass()).info(
                "Initialization --> ...Prepare configuration for Bundle = {}",
                this.caller().getSymbolicName()
            );
        }
    }
}