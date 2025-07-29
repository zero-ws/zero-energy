package io.zerows.core.feature.web.security.osgi;

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
 * @author lang : 2024-04-17
 */
@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class WebSecurityActivator extends DependencyActivatorBase {
    @Override
    public void init(final BundleContext context, final DependencyManager dm) throws Exception {
        final Bundle bundle = context.getBundle();


        // Dependency
        final ServiceConnector connector = WebSecurityDependency.of(bundle);

        connector.serviceDependency(dm, this::createComponent, this::createServiceDependency);
        connector.serviceRegister(dm, this::createComponent);

        Ut.Log.bundle(this.getClass())
            .info(OMessage.Osgi.BUNDLE.START, bundle.getSymbolicName());
    }
}
