package io.zerows.core.configuration.uca.setup;

import io.horizon.eon.em.web.ServerType;

/**
 * @author lang : 2024-04-20
 */
class ProcessorOfRx extends ProcessorOfHttp {

    @Override
    protected ServerType typeOfHttp() {
        return ServerType.RX;
    }
}
