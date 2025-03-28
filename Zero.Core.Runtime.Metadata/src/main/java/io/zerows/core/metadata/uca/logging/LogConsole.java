package io.zerows.core.metadata.uca.logging;

import io.horizon.spi.HorizonIo;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.vertx.up.util.Ut;

/**
 * @author lang : 2024-04-17
 */
class LogConsole implements OLog {
    private static final String MODULE = "Zero";

    private final transient LogModule internalLogger;
    private final transient Class<?> targetClass;

    LogConsole(final Class<?> clazz, final String name) {
        this.targetClass = clazz;

        final HorizonIo io = Ut.Bnd.serviceIo(clazz);
        this.internalLogger = Log.modulat(MODULE).osgi(name, io);
    }

    @Override
    public void info(final String pattern, final Object... args) {
        this.internalLogger.info(this.targetClass, pattern, args);
    }

    @Override
    public void debug(final String pattern, final Object... args) {
        this.internalLogger.debug(this.targetClass, pattern, args);
    }

    @Override
    public void warn(final String pattern, final Object... args) {
        this.internalLogger.warn(this.targetClass, pattern, args);
    }

    @Override
    public void error(final String pattern, final Object... args) {
        this.internalLogger.error(this.targetClass, pattern, args);
    }

    @Override
    public void fatal(final Throwable ex) {
        this.internalLogger.fatal(this.targetClass, ex);
    }

    @Override
    public void fatal(final Throwable ex, final String prefix) {
        this.internalLogger.fatal(this.targetClass, ex, prefix);
    }
}
