package io.zerows.core.configuration.uca.setup;

import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.config.HSetting;
import io.vertx.core.VertxOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.configuration.atom.NodeNetwork;
import io.zerows.core.configuration.atom.NodeVertx;
import io.zerows.core.configuration.atom.option.ClusterOptions;
import io.zerows.core.configuration.uca.transformer.ClusterTransformer;
import io.zerows.core.configuration.uca.transformer.VertxTransformer;
import io.zerows.core.configuration.zdk.Processor;
import io.zerows.core.configuration.zdk.Transformer;
import io.zerows.core.metadata.uca.stable.Ruler;

/**
 * 新版 VertxOptions 和 ClusterOptions 的构造流程
 *
 * @author lang : 2024-04-20
 */
class VertxProcessor implements Processor<NodeNetwork, HSetting> {
    private final transient Transformer<VertxOptions> transformerVertx;
    private final transient Transformer<ClusterOptions> transformerCluster;

    VertxProcessor() {
        this.transformerVertx = Ut.singleton(VertxTransformer.class);
        this.transformerCluster = Ut.singleton(ClusterTransformer.class);
    }

    @Override
    public void makeup(final NodeNetwork network, final HSetting setting) {
        // 访问 containerJ 中的容器基础配置信息
        final HConfig container = setting.container();
        final JsonObject containerJ = container.options();


        // 提取 Vertx 基本配置
        final JsonObject vertxData = Ut.valueJObject(containerJ, YmlCore.VERTX);
        this.logger().debug(INFO.V_BEFORE, YmlCore.VERTX,
            this.getClass().getName(), vertxData);


        // 配置验证
        Fn.jvmAt(() -> Ruler.verify(YmlCore.VERTX, vertxData), vertxData);


        // 集群基础配置
        final JsonObject clusterData = Ut.valueJObject(vertxData, YmlCore.vertx.CLUSTERED);
        final ClusterOptions clusterOptions = this.transformerCluster.transform(clusterData);


        // Vertx 基础配置
        final boolean clustered = clusterOptions.isEnabled();
        if (clustered) {
            // VertxBuilder 最终构造（ClusterManager新版在 Builder 中处理）
            network.cluster(clusterOptions);
        }
        final JsonArray instanceData = Ut.valueJArray(vertxData, YmlCore.vertx.INSTANCE);
        Ut.itJArray(instanceData).forEach(instanceJ -> {


            // Vertx 名称
            final String name = Ut.valueString(instanceJ, KName.NAME);
            // VertxOption 配置绑定
            final VertxOptions vertxOptions = this.transformerVertx.transform(instanceJ);


            // 更新基础配置
            final NodeVertx nodeVertx = NodeVertx.of(name, network);
            nodeVertx.optionVertx(vertxOptions);
            network.add(name, nodeVertx);
        });
    }
}
