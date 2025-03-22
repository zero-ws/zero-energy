package io.zerows.core.web.model.exception;

import io.horizon.exception.BootingException;

public class BootCodexMoreException extends BootingException {

    public BootCodexMoreException(final Class<?> clazz,
                                  final Class<?> target) {
        super(clazz, target);
    }

    @Override
    public int getCode() {
        return -40036;
    }
}
