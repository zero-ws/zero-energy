package io.zerows.core.metadata.uca.execution;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Future;
import io.vertx.up.util.Ut;
import io.zerows.core.metadata.atom.service.CallbackParameter;
import io.zerows.core.metadata.uca.logging.OLog;
import io.zerows.core.metadata.zdk.service.ServiceContext;
import io.zerows.core.metadata.zdk.service.ServiceInvocation;
import org.osgi.framework.Bundle;

/**
 * 服务执行器，执行服务专用，从不同的启动流程执行服务
 * <pre><code>
 *     Provider 执行
 *     Consumer 执行
 *     Context 执行
 * </code></pre>
 *
 * @author lang : 2024-07-01
 */
public interface ServiceRunner {

    Cc<Long, ServiceRunner> CC_SKELETON = Cc.open();

    static ServiceRunner of(final Bundle owner) {
        return CC_SKELETON.pick(() -> new ServiceRunnerEntry(owner), owner.getBundleId());
    }

    void start(CallbackParameter parameter);

    default void stop(final CallbackParameter parameter) {
    }


    default OLog logger() {
        return Ut.Log.service(this.getClass());
    }

    interface Exec {

        static Future<String> startInvoke(final ServiceInvocation invocation,
                                          final ServiceContext context) {
            final Bundle consumer = context.owner();
            Ut.Log.service(invocation.getClass())
                .info("When Start, Service {} will be called on: {}", invocation.id(), consumer.getSymbolicName());
            return invocation.start(context).compose(called -> {
                if (called) {
                    // 历史记录
                    final ServiceExecuted executed = ServiceExecuted.of(consumer);
                    executed.addExecuted(consumer.getBundleId(), invocation.id());
                    return Future.succeededFuture(invocation.id());
                } else {
                    return Future.succeededFuture();
                }
            });
        }

        static Future<String> stopInvoke(final ServiceInvocation invocation,
                                         final ServiceContext context) {
            final Bundle consumer = context.owner();
            Ut.Log.service(invocation.getClass())
                .info("When Stop, Service {} will be called on: {}", invocation.id(), consumer.getSymbolicName());
            return invocation.stop(context).compose(called -> {
                if (called) {
                    // 历史记录
                    final ServiceExecuted executed = ServiceExecuted.of(consumer);
                    executed.removeExecuted(consumer.getBundleId(), invocation.id());
                    return Future.succeededFuture(invocation.id());
                } else {
                    return Future.succeededFuture();
                }
            });
        }
    }
}
