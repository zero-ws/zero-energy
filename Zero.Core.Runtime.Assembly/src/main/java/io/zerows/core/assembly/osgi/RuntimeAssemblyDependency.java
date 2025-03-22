package io.zerows.core.assembly.osgi;

import io.zerows.core.assembly.osgi.service.EnergyClass;
import io.zerows.core.assembly.osgi.service.EnergyClassService;
import io.zerows.core.assembly.osgi.service.provider.InvocationAssembly;
import io.zerows.core.metadata.eon.OMessage;
import io.zerows.core.metadata.zdk.dependency.AbstractConnectorService;
import io.zerows.core.metadata.zdk.service.ServiceConnector;
import io.zerows.core.metadata.zdk.service.ServiceInvocation;
import org.apache.felix.dm.Component;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.Bundle;

import java.util.function.Supplier;

/**
 * @author lang : 2024-04-22
 */
class RuntimeAssemblyDependency extends AbstractConnectorService {
    private RuntimeAssemblyDependency(final Bundle bundle) {
        super(bundle);
    }

    static ServiceConnector of(final Bundle bundle) {
        return of(bundle, RuntimeAssemblyDependency::new);
    }

    @Override
    public void serviceRegister(final DependencyManager dm, final Supplier<Component> supplier) {
        // Enroll / 元数据管理服务
        dm.add(supplier.get().setInterface(EnergyClass.class, null)
            .setImplementation(EnergyClassService.class));
        this.logger().info(OMessage.Osgi.SERVICE.REGISTER, EnergyClass.class, EnergyClassService.class);
    }

    @Override
    protected ServiceInvocation[] withProviders(final Bundle provider) {
        return new ServiceInvocation[]{
            new InvocationAssembly(provider) // 扫描组件专用服务
        };
    }
}
