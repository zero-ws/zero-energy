package io.zerows.core.web.container.uca.store;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.uca.environment.DevOps;
import io.zerows.core.web.model.atom.running.RunVertx;

import java.util.Objects;
import java.util.Set;

/**
 * @author lang : 2024-05-03
 */
@SuppressWarnings("all")
class LinearTool {

    static void startAsync(final Class<?> verticleCls,
                           final DeploymentOptions options,
                           final RunVertx runVertx) {
        final Vertx vertx = runVertx.instance();
        Objects.requireNonNull(vertx);
        // 发布启动
        vertx.deployVerticle(() -> Ut.instance(verticleCls), options, (res) -> {
            final String deploymentId = res.result();
            if (res.succeeded()) {
                Ut.Log.vertx(verticleCls).info(INFO.SUCCESS_STARTED,
                    // 0,1,2,3
                    verticleCls.getName(), options.getInstances(), deploymentId,
                    options.getThreadingModel());


                // DevOps
                DevOps.on(vertx).add(verticleCls, options, deploymentId);


                runVertx.deploymentAdd(deploymentId, verticleCls);
            } else {
                final Throwable ex = res.cause();
                ex.printStackTrace();


                Ut.Log.vertx(verticleCls).warn(INFO.FAILURE_STARTED,
                    verticleCls.getName(), options.getInstances(), deploymentId,
                    ex.getMessage(), options.getThreadingModel());
            }
        });
    }

    static void stopAsync(final Class<?> verticleCls,
                          final DeploymentOptions options,
                          final RunVertx runVertx) {
        final Vertx vertx = runVertx.instance();
        Objects.requireNonNull(vertx);
        // 发布撤销
        final Set<String> ids = runVertx.deploymentFind(verticleCls);
        ids.forEach(id -> vertx.undeploy(id, res -> {
            if (res.succeeded()) {
                Ut.Log.vertx(verticleCls).info(INFO.SUCCESS_STOPPED,
                    // 0,1,2
                    verticleCls.getName(), id, options.getThreadingModel());


                // DevOps
                DevOps.on(vertx).remove(verticleCls, options);


                runVertx.deploymentRemove(id);
            }
        }));
    }
}
