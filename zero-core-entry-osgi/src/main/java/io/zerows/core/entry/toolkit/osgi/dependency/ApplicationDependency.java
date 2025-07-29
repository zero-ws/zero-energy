package io.zerows.core.entry.toolkit.osgi.dependency;

import io.zerows.core.assembly.osgi.service.EnergyClass;
import io.zerows.core.configuration.osgi.service.EnergyOption;
import io.zerows.core.metadata.atom.service.CallbackParameter;
import io.zerows.core.metadata.osgi.dependency.CallbackOfService;
import io.zerows.core.metadata.osgi.service.EnergyConfiguration;
import io.zerows.core.metadata.osgi.service.EnergyDeployment;
import io.zerows.core.metadata.osgi.service.EnergyFailure;
import io.zerows.core.metadata.osgi.service.EnergyService;
import io.zerows.core.metadata.zdk.dependency.AbstractConnectorService;
import io.zerows.core.metadata.zdk.service.ServiceConnector;
import io.zerows.core.metadata.zdk.service.ServiceContext;
import io.zerows.core.metadata.zdk.service.ServiceInvocation;
import org.osgi.framework.Bundle;

import java.util.Objects;

/**
 * 重新设计的执行生命周期，为了兼容多应用模式
 * <pre><code>
 *     1. 启动所有插件
 *        - 异常管理器，下层对接可扩展异常处理，依赖服务 {@link EnergyFailure}
 *        - 服务管理器（新）
 *     2. 将服务注册到服务管理器中形成 Provider / Consumer 服务
 *     3. 上下文启动，启动完成之后执行上下文信号量处理
 *        ServiceContext.OK
 *     4. 根据上下文执行 HApp / HArk 的应用准备流程
 *     5. 内核启动：
 *        - Vertx                         依赖服务 {@link EnergyOption}
 *        - Server
 *        - Router
 *        - Storage
 *        - Security
 *     6. 应用准备完成之后执行服务 Provider 中的所有服务（调用）
 * </code></pre>
 *
 * @author lang : 2024-07-01
 */
public class ApplicationDependency extends AbstractConnectorService {
    protected ApplicationDependency(final ServiceContext context) {
        super(context.owner());
    }

    public static ServiceConnector of(final ServiceContext context) {
        Objects.requireNonNull(context);
        return of(context.owner(), (bnd) -> new ApplicationDependency(context));
    }
    // ------------- 子类必须重写的方法 -------------

    @Override
    protected CallbackOfService withComponent(final CallbackParameter parameter) {
        return new ApplicationCallback(parameter);
    }

    @Override
    protected String[] withConsumers(final Bundle owner) {
        return new String[]{
            ServiceInvocation.ISV.INVOCATION_CONTAINER
        };
    }

    @Override
    protected Class<?>[] buildDependency() {
        return new Class<?>[]{
            // 配置管理 / 服务管理 / 部署管理
            EnergyConfiguration.class,
            EnergyService.class,
            EnergyDeployment.class,

            
            // 扫描服务专用
            EnergyClass.class
        };
    }
}
