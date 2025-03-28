package io.zerows.core.metadata.uca.execution;

import io.zerows.core.metadata.atom.service.CallbackParameter;
import org.osgi.framework.Bundle;

/**
 * @author lang : 2024-07-02
 */
class ServiceRunnerContext extends AbstractServiceRunner {
    ServiceRunnerContext(final Bundle owner) {
        super(owner);
    }

    @Override
    public void start(final CallbackParameter parameter) {
        // Context 上线，上线之后没有任何服务记录，所以读取所有生产者和消费者
    }

    @Override
    public void stop(final CallbackParameter parameter) {

    }
}
