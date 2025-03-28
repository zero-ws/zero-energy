package io.zerows.core.metadata.uca.execution;

import io.vertx.core.Future;
import io.vertx.up.fn.Fn;
import io.zerows.core.metadata.atom.service.CallbackParameter;
import io.zerows.core.metadata.zdk.service.ServiceInvocation;
import org.osgi.framework.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author lang : 2024-07-02
 */
class ServiceRunnerConsumer extends AbstractServiceRunner {
    ServiceRunnerConsumer(final Bundle owner) {
        super(owner);
    }

    @Override
    public void start(final CallbackParameter parameter) {
        final Set<ServiceInvocation> waitServices = this.getServices(parameter);

        final List<Future<String>> executed = new ArrayList<>();
        waitServices.stream().map(invocation -> Exec.startInvoke(invocation, parameter.context()))
            .forEach(executed::add);

        Fn.combineT(executed).onComplete(this::handleExecuted);
    }

    @Override
    public void stop(final CallbackParameter parameter) {
        final Set<ServiceInvocation> waitServices = this.getServices(parameter);

        final List<Future<String>> executed = new ArrayList<>();
        waitServices.stream().map(invocation -> Exec.stopInvoke(invocation, parameter.context()))
            .forEach(executed::add);

        Fn.combineT(executed).onComplete(this::handleExecuted);
    }

    private Set<ServiceInvocation> getServices(final CallbackParameter parameter) {
        final Set<String> serviceIds = parameter.consumers();
        // 执行记录检查
        final Bundle consumer = parameter.context().owner();
        return this.waitServicesById(serviceIds, consumer);
    }
}
