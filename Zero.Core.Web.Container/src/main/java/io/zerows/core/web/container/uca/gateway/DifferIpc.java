package io.zerows.core.web.container.uca.gateway;

import io.horizon.uca.log.Annal;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.web.container.exception.BootReturnTypeException;
import io.zerows.core.web.container.uca.mode.AimIpc;
import io.zerows.core.web.io.zdk.Aim;
import io.zerows.core.web.model.atom.Event;

import java.lang.reflect.Method;

class DifferIpc implements Differ<RoutingContext> {

    private static final Annal LOGGER = Annal.get(DifferIpc.class);

    private DifferIpc() {
    }

    public static Differ<RoutingContext> create() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public Aim<RoutingContext> build(final Event event) {
        final Method method = event.getAction();
        final Class<?> returnType = method.getReturnType();
        // Rpc Mode only
        Aim<RoutingContext> aim = null;
        if (Void.class == returnType || void.class == returnType) {
            // Exception because this method must has return type to
            // send message to event bus. It means that it require
            // return types.
            Fn.outBoot(true, LOGGER, BootReturnTypeException.class,
                this.getClass(), method);
        } else {
            // Mode 6: Ipc channel enabled
            aim = CACHE.CC_AIMS.pick(() -> Ut.instance(AimIpc.class), "Mode Ipc");
            // Fn.po?l(Pool.AIMS, Thread.currentThread().getName() + "-mode-ipc",() -> Ut.instance(IpcAim.class));
        }
        return aim;
    }

    private static final class InstanceHolder {
        private static final Differ<RoutingContext> INSTANCE = new DifferIpc();
    }
}
