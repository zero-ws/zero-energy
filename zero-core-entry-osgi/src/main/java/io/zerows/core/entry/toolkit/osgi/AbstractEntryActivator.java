package io.zerows.core.entry.toolkit.osgi;

import io.zerows.core.metadata.zdk.service.ServiceConnector;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

/**
 * 入口 Bundle 统一程序，追加服务管理流程，用来管理 OSGI 中的 ServiceProvider / ServiceConsumer 部分，整体功能如下
 * <pre><code>
 *     1. 提供依赖服务清单
 *     2. 使用标准化的 ServiceConnector
 *     3. 使用新版的 Callback
 * </code></pre>
 *
 * @author lang : 2024-07-01
 */
public abstract class AbstractEntryActivator extends DependencyActivatorBase {

    @Override
    public void init(final BundleContext context, final DependencyManager dm) throws Exception {
        this.initPre(context, dm);

        final ServiceConnector dependency = this.buildConnector(context.getBundle(), dm);
        dependency.serviceRegister(dm, this::createComponent);
        dependency.serviceDependency(dm, this::createComponent, this::createServiceDependency);

        this.initPost(context, dm);
    }

    @Override
    public void destroy(final BundleContext context, final DependencyManager dm) throws Exception {
        // ....
        this.destroyPost(context, dm);
    }

    // ---------------- 子类可重写的生命周期管理函数 ----------------
    protected void initPre(final BundleContext context, final DependencyManager dm) {
        // 子类可重写
    }

    protected void initPost(final BundleContext context, final DependencyManager dm) {
        // 子类可重写
    }

    protected void destroyPost(final BundleContext context, final DependencyManager dm) {
        // 子类可重写
    }

    // ---------------- 子类必须构造 ServiceConnector ----------------
    protected abstract ServiceConnector buildConnector(final Bundle owner, final DependencyManager dm);
}
