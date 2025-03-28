package io.zerows.core.feature.web.security.exception;

import io.horizon.exception.BootingException;

public class BootWallMethodDuplicatedException extends BootingException {

    public BootWallMethodDuplicatedException(final Class<?> clazz,
                                             final String annoCls,
                                             final String targetCls) {
        super(clazz, annoCls, targetCls);
    }

    @Override
    public int getCode() {
        return -40041;
    }
}
