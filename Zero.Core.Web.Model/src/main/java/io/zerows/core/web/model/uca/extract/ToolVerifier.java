package io.zerows.core.web.model.uca.extract;

import io.horizon.uca.log.Annal;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;
import io.zerows.core.web.model.exception.BootAccessProxyException;
import io.zerows.core.web.model.exception.BootNoArgConstructorException;

import java.lang.reflect.Modifier;

public class ToolVerifier {

    public static void noArg(final Class<?> clazz, final Class<?> target) {
        final Annal logger = Annal.get(target);
        Fn.outBoot(!Ut.isDefaultConstructor(clazz), logger,
            BootNoArgConstructorException.class,
            logger, clazz);
    }

    public static void modifier(final Class<?> clazz, final Class<?> target) {
        final Annal logger = Annal.get(target);
        Fn.outBoot(!Modifier.isPublic(clazz.getModifiers()), logger,
            BootAccessProxyException.class,
            target, clazz);
    }
}
