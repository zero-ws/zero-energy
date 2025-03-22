package io.zerows.core.web.container.exception;

import io.horizon.eon.em.web.ServerType;
import io.horizon.exception.BootingException;
import io.vertx.up.util.Ut;

import java.util.Set;

public class BootAgentDuplicatedException extends BootingException {

    public BootAgentDuplicatedException(final Class<?> clazz,
                                        final ServerType type,
                                        final int numbber,
                                        final Set<String> agents) {
        super(clazz, numbber, type, Ut.fromJoin(agents));
    }

    @Override
    public int getCode() {
        return -40004;
    }
}
