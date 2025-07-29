package io.zerows.core.web.model.osgi;

import io.vertx.up.util.Ut;
import io.zerows.core.metadata.eon.OMessage;
import io.zerows.core.metadata.zdk.service.ServiceConnector;
import org.apache.felix.dm.DependencyActivatorBase;
import org.apache.felix.dm.DependencyManager;
import org.osgi.annotation.bundle.Header;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;

/**
 * @author lang : 2024-07-01
 */
@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class WebModelActivator extends DependencyActivatorBase {
    @Override
    public void init(final BundleContext context, final DependencyManager dm) throws Exception {
        final Bundle bundle = context.getBundle();


        // Dependency
        final ServiceConnector connector = WebModelDependency.of(bundle);

        connector.serviceRegister(dm, this::createComponent);
        connector.serviceDependency(dm, this::createComponent, this::createServiceDependency);

        
        Ut.Log.bundle(this.getClass())
            .info(OMessage.Osgi.BUNDLE.START, bundle.getSymbolicName());
    }
}
