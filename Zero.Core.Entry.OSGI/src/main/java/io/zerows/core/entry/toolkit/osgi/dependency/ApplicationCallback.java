package io.zerows.core.entry.toolkit.osgi.dependency;

import io.zerows.core.assembly.osgi.dependency.OnceAssembly;
import io.zerows.core.assembly.osgi.service.EnergyClass;
import io.zerows.core.metadata.atom.service.CallbackParameter;
import io.zerows.core.metadata.eon.OMessage;
import io.zerows.core.metadata.osgi.dependency.CallbackOfService;
import io.zerows.core.metadata.osgi.dependency.OnceConfiguration;
import io.zerows.core.metadata.osgi.dependency.OnceService;
import io.zerows.core.metadata.osgi.dependency.OnceServiceContext;
import io.zerows.core.metadata.osgi.service.EnergyService;
import io.zerows.core.metadata.zdk.dependency.OOnce;
import io.zerows.core.metadata.zdk.service.ServiceContext;

/**
 * @author lang : 2024-07-01
 */
public class ApplicationCallback extends CallbackOfService {

    private final OOnce<EnergyClass> onceClass;

    /**
     * 上层包含了
     * <pre><code>
     *     {@link OnceConfiguration}
     *     {@link OnceService}
     * </code></pre>
     *
     * @param parameter {@link CallbackParameter}
     */
    public ApplicationCallback(final CallbackParameter parameter) {
        super(parameter);
        this.onceClass = new OnceAssembly();
    }

    @Override
    protected OOnce<EnergyService> ofOnceService() {
        return new OnceServiceContext();
    }

    @Override
    protected void startBind(final Object reference) {
        this.onceClass.bind(reference);
    }

    @Override
    public synchronized boolean isReady() {
        return super.isReady()
            && this.onceClass.isReady();
    }

    @Override
    public void startEnd(final Object reference) {
        final CallbackParameter parameter = this.ofParameter();
        final ServiceContext context = parameter.context();
        final Class<?> serviceImpl = this.startFinished(ServiceContext.OK.class, context::signalClass);
        this.logger().info(OMessage.Osgi.SERVICE.DELAY, serviceImpl);
    }
}
