package io.zerows.core.web.security.exception;

import io.horizon.exception.BootingException;
import io.vertx.up.eon.em.EmSecure;

public class BootWallSizeException extends BootingException {

    public BootWallSizeException(final Class<?> clazz,
                                 final EmSecure.AuthWall wall,
                                 final Integer size) {
        super(clazz, wall.key(), size);
    }

    @Override
    public int getCode() {
        return -40076;
    }
}
