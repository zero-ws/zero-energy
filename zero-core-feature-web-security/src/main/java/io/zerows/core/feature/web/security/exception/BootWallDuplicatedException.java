package io.zerows.core.feature.web.security.exception;

import io.horizon.exception.BootingException;
import io.vertx.up.util.Ut;

import java.util.Set;

public class BootWallDuplicatedException extends BootingException {

    public BootWallDuplicatedException(final Class<?> classes,
                                       final Set<String> classNames) {
        super(classes, Ut.fromJoin(classNames));
    }

    @Override
    public int getCode() {
        return -40038;
    }
}
