package io.zerows.core.web.container.uca.store;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.up.util.Ut;
import io.zerows.core.configuration.atom.NodeVertx;
import io.zerows.core.configuration.atom.option.ActorTool;
import io.zerows.core.metadata.eon.em.EmDeploy;
import io.zerows.core.metadata.zdk.AbstractAmbiguity;
import io.zerows.core.web.model.atom.running.RunVertx;
import org.osgi.framework.Bundle;

import java.util.Objects;

/**
 * @author lang : 2024-05-03
 */
class LinearWorker extends AbstractAmbiguity implements StubLinear {
    LinearWorker(final Bundle bundle) {
        super(bundle);
    }

    @Override
    public void runDeploy(final Class<?> clazz, final RunVertx runVertx) {
        final DeploymentOptions options = this.buildOptions(clazz, runVertx);

        if (Objects.isNull(options)) {
            return;
        }

        LinearTool.startAsync(clazz, options, runVertx);

        Runtime.getRuntime().addShutdownHook(
            new Thread(() -> LinearTool.stopAsync(clazz, options, runVertx))
        );
    }

    @Override
    public void runUndeploy(final Class<?> clazz, final RunVertx runVertx) {
        final DeploymentOptions options = this.buildOptions(clazz, runVertx);

        if (Objects.isNull(options)) {
            return;
        }

        LinearTool.stopAsync(clazz, options, runVertx);
    }

    private DeploymentOptions buildOptions(final Class<?> clazz, final RunVertx runVertx) {
        final NodeVertx nodeVertx = runVertx.config();
        DeploymentOptions options = nodeVertx.optionDeployment(clazz);
        if (Objects.isNull(options)) {
            /*
             * Fix: Exception in thread "component-worker-218" java.lang.NullPointerException:
             * Cannot invoke "io.vertx.core.DeploymentOptions.getThreadingModel()" because "options" is null
             */
            options = new DeploymentOptions();
            ActorTool.setupWith(options, clazz, EmDeploy.Mode.CODE);
            return options;
        }

        // Verticle Deployment
        final ThreadingModel threadModel = options.getThreadingModel();
        if (ThreadingModel.EVENT_LOOP == threadModel) {
            this.logger().warn(INFO.THREAD_NOT_MATCH, Ut.fromJoin(new String[]{
                ThreadingModel.WORKER.name(),
                ThreadingModel.VIRTUAL_THREAD.name()
            }), threadModel);
            return null;
        }
        return options;
    }
}
