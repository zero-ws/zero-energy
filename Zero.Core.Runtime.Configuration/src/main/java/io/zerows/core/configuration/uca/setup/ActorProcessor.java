package io.zerows.core.configuration.uca.setup;

import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.config.HSetting;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;
import io.zerows.core.configuration.atom.NodeVertx;
import io.zerows.core.configuration.atom.option.ActorOptions;
import io.zerows.core.configuration.uca.transformer.ActorTransformer;
import io.zerows.core.configuration.zdk.Processor;
import io.zerows.core.configuration.zdk.Transformer;

import java.util.Objects;

/**
 * @author lang : 2024-04-20
 */
class ActorProcessor implements Processor<NodeVertx, HSetting> {
    private final transient Transformer<ActorOptions> transformerActor;

    ActorProcessor() {
        this.transformerActor = Ut.singleton(ActorTransformer.class);
    }

    @Override
    public void makeup(final NodeVertx target, final HSetting setting) {
        if (!setting.hasInfix(YmlCore.deployment.__KEY)) {
            this.logger().info(INFO.V_DEPLOYMENT);
            return;
        }
        final HConfig config = setting.infix(YmlCore.deployment.__KEY);
        if (Objects.isNull(config)) {
            return;
        }
        final JsonObject options = config.options();
        if (Ut.isNil(options)) {
            return;
        }

        // DeliveryOptions and DeploymentOptions
        final ActorOptions actorOptions = this.transformerActor.transform(options);
        target.optionDelivery(actorOptions.optionDelivery());
        /*
         * 此处的模式设置很重要，根据 DeploymentOptions 的发布模式可以判断环境中是否会计算两个参数的优先级
         * 1）CODE 模式，类 Class 中定义的优先
         * 2）CONFIG 模式，直接忽略类 Class 中定义的优先级
         * 不论哪种模式，线程池都会根据 Class 的定义有所修正
         * 1）@Agent，线程池只能是 EVENT_LOOP
         * 2）@Worker，线程池只能是 WORKER 或 VIRTUAL_THREAD，且 VIRTUAL_THREAD 模式下如果 Worker 模式下类型匹配则直接忽略不修改，
         *    即只支持 WORKER -> VIRTUAL_THREAD 的单向修正模式
         */
        target.mode(actorOptions.getMode());
        actorOptions.optionDeploy().forEach(target::optionDeployment);
    }
}
